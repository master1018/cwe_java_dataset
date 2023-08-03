
package org.projectforge.web.admin;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.projectforge.web.wicket.AbstractForm;
import org.projectforge.web.wicket.CsrfTokenHandler;
import org.projectforge.web.wicket.bootstrap.GridBuilder;
import org.projectforge.web.wicket.components.SingleButtonPanel;
import org.projectforge.web.wicket.flowlayout.FieldsetPanel;
import org.projectforge.web.wicket.flowlayout.FileUploadPanel;
public class SetupImportForm extends AbstractForm<SetupImportForm, SetupPage>
{
  private static final long serialVersionUID = -277853572580468505L;
  protected FileUploadField fileUploadField;
  protected String filename;
  private final CsrfTokenHandler csrfTokenHandler;
  public SetupImportForm(final SetupPage parentPage)
  {
    super(parentPage, "importform");
    initUpload(Bytes.megabytes(100));
    csrfTokenHandler = new CsrfTokenHandler(this);
  }
  @Override
  protected void onSubmit()
  {
    super.onSubmit();
    csrfTokenHandler.onSubmit();
  }
  @Override
  @SuppressWarnings("serial")
  protected void init()
  {
    add(createFeedbackPanel());
    final GridBuilder gridBuilder = newGridBuilder(this, "flowform");
    gridBuilder.newFormHeading(getString("import"));
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.setup.dumpFile"));
      fileUploadField = new FileUploadField(FileUploadPanel.WICKET_ID);
      fs.add(new FileUploadPanel(fs.newChildId(), fileUploadField));
    }
    final RepeatingView actionButtons = new RepeatingView("buttons");
    add(actionButtons);
    {
      final Button importButton = new Button(SingleButtonPanel.WICKET_ID, new Model<String>("import")) {
        @Override
        public final void onSubmit()
        {
          parentPage.upload();
        }
      };
      final SingleButtonPanel importButtonPanel = new SingleButtonPanel(actionButtons.newChildId(), importButton, getString("import"),
          SingleButtonPanel.DEFAULT_SUBMIT);
      actionButtons.add(importButtonPanel);
    }
  }
}
