
package org.projectforge.web.admin;
import java.util.SortedSet;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.projectforge.Version;
import org.projectforge.continuousdb.UpdateEntry;
import org.projectforge.continuousdb.UpdatePreCheckStatus;
import org.projectforge.web.HtmlHelper;
import org.projectforge.web.wicket.AbstractForm;
import org.projectforge.web.wicket.CsrfTokenHandler;
import org.projectforge.web.wicket.bootstrap.GridBuilder;
import org.projectforge.web.wicket.components.SingleButtonPanel;
import org.projectforge.web.wicket.flowlayout.CheckBoxPanel;
import org.projectforge.web.wicket.flowlayout.FieldsetPanel;
import org.projectforge.web.wicket.flowlayout.MyComponentsRepeater;
public class SystemUpdateForm extends AbstractForm<SystemUpdateForm, SystemUpdatePage>
{
  private static final long serialVersionUID = 2492737003121592489L;
  protected WebMarkupContainer scripts;
  public boolean showOldUpdateScripts;
  private GridBuilder gridBuilder;
  private final CsrfTokenHandler csrfTokenHandler;
  protected MyComponentsRepeater<SingleButtonPanel> actionButtons;
  public SystemUpdateForm(final SystemUpdatePage parentPage)
  {
    super(parentPage);
    csrfTokenHandler = new CsrfTokenHandler(this);
  }
  @Override
  @SuppressWarnings("serial")
  protected void init()
  {
    add(createFeedbackPanel());
    gridBuilder = newGridBuilder(this, "flowform");
    gridBuilder.newGridPanel();
    {
      final FieldsetPanel fs = gridBuilder.newFieldset("Show all");
      fs.add(new CheckBoxPanel(fs.newChildId(), new PropertyModel<Boolean>(this, "showOldUpdateScripts"), null, true) {
        @Override
        protected void onSelectionChanged(final Boolean newSelection)
        {
          parentPage.refresh();
        }
      });
    }
    scripts = new WebMarkupContainer("scripts");
    add(scripts);
    updateEntryRows();
    actionButtons = new MyComponentsRepeater<SingleButtonPanel>("buttons");
    add(actionButtons.getRepeatingView());
    {
      final Button refreshButton = new Button(SingleButtonPanel.WICKET_ID, new Model<String>("refresh")) {
        @Override
        public final void onSubmit()
        {
          parentPage.refresh();
        }
      };
      final SingleButtonPanel refreshButtonPanel = new SingleButtonPanel(actionButtons.newChildId(), refreshButton, "refresh",
          SingleButtonPanel.DEFAULT_SUBMIT);
      actionButtons.add(refreshButtonPanel);
      setDefaultButton(refreshButton);
    }
  }
  @SuppressWarnings("serial")
  protected void updateEntryRows()
  {
    scripts.removeAll();
    final RepeatingView scriptRows = new RepeatingView("scriptRows");
    scripts.add(scriptRows);
    final SortedSet<UpdateEntry> updateEntries = parentPage.myDatabaseUpdater.getSystemUpdater().getUpdateEntries();
    if (updateEntries == null) {
      return;
    }
    boolean odd = true;
    for (final UpdateEntry updateEntry : updateEntries) {
      if (showOldUpdateScripts == false && updateEntry.getPreCheckStatus() == UpdatePreCheckStatus.ALREADY_UPDATED) {
        continue;
      }
      final Version version = updateEntry.getVersion();
      final WebMarkupContainer item = new WebMarkupContainer(scriptRows.newChildId());
      scriptRows.add(item);
      if (odd == true) {
        item.add(AttributeModifier.append("class", "odd"));
      } else {
        item.add(AttributeModifier.append("class", "even"));
      }
      odd = !odd;
      item.add(new Label("regionId", updateEntry.getRegionId()));
      if (updateEntry.isInitial() == true) {
        item.add(new Label("version", "initial"));
      } else {
        item.add(new Label("version", version.toString()));
      }
      final String description = updateEntry.getDescription();
      item.add(new Label("description", StringUtils.isBlank(description) == true ? "" : description));
      item.add(new Label("date", updateEntry.getDate()));
      final String preCheckResult = updateEntry.getPreCheckResult();
      item.add(new Label("preCheckResult", HtmlHelper.escapeHtml(preCheckResult, true)));
      if (updateEntry.getPreCheckStatus() == UpdatePreCheckStatus.READY_FOR_UPDATE) {
        final Button updateButton = new Button("button", new Model<String>("update")) {
          @Override
          public final void onSubmit()
          {
            parentPage.update(updateEntry);
          }
        };
        item.add(new SingleButtonPanel("update", updateButton, "update"));
      } else {
        final String runningResult = updateEntry.getRunningResult();
        item.add(new Label("update", HtmlHelper.escapeHtml(runningResult, true)));
      }
    }
  }
  @Override
  public void onBeforeRender()
  {
    super.onBeforeRender();
    actionButtons.render();
  }
  @Override
  protected void onSubmit()
  {
    super.onSubmit();
    csrfTokenHandler.onSubmit();
  }
}
