
package org.projectforge.web.mobile;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.projectforge.core.AbstractBaseDO;
import org.projectforge.web.wicket.mobileflowlayout.MobileGridBuilder;
public abstract class AbstractMobileEditForm<O extends AbstractBaseDO< ? >, P extends AbstractMobileEditPage< ? , ? , ? >> extends
AbstractMobileForm<O, P>
{
  private static final long serialVersionUID = 1836099012618517190L;
  protected O data;
  protected MobileGridBuilder gridBuilder;
  public AbstractMobileEditForm(final P parentPage, final O data)
  {
    super(parentPage);
    this.data = data;
  }
  public O getData()
  {
    return this.data;
  }
  public void setData(final O data)
  {
    this.data = data;
  }
  public boolean isNew()
  {
    return this.data == null || this.data.getId() == null;
  }
  @SuppressWarnings("serial")
  protected void init()
  {
    add(new FeedbackPanel("feedback").setOutputMarkupId(true));
    final SubmitLink submitButton = new SubmitLink("submitButton") {
      @Override
      public final void onSubmit()
      {
        parentPage.save();
      }
    };
    final RepeatingView flowform = new RepeatingView("flowform");
    add(flowform);
    gridBuilder = newGridBuilder(flowform);
    add(submitButton);
    if (isNew() == true) {
      submitButton.add(new Label("label", getString("create")));
    } else {
      submitButton.add(new Label("label", getString("update")));
    }
  }
}
