
package org.projectforge.web.dialog;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.projectforge.web.core.NavTopPanel;
import org.projectforge.web.wicket.CsrfTokenHandler;
import org.projectforge.web.wicket.WicketUtils;
import org.projectforge.web.wicket.bootstrap.GridBuilder;
import org.projectforge.web.wicket.components.SingleButtonPanel;
import org.projectforge.web.wicket.flowlayout.MyComponentsRepeater;
import de.micromata.wicket.ajax.AjaxCallback;
import de.micromata.wicket.ajax.AjaxFormSubmitCallback;
public abstract class ModalDialog extends Panel
{
  private static final long serialVersionUID = 4235521713603821639L;
  protected GridBuilder gridBuilder;
  protected final WebMarkupContainer mainContainer, mainSubContainer, gridContentContainer, buttonBarContainer;
  private boolean escapeKeyEnabled = true;
  private String closeButtonLabel;
  private SingleButtonPanel closeButtonPanel;
  private boolean showCancelButton;
  private boolean bigWindow;
  private boolean draggable = true;
  private Boolean resizable;
  private boolean lazyBinding;
  private WebMarkupContainer titleContainer;
  private Label titleLabel;
  protected Form< ? > form;
  protected FeedbackPanel formFeedback;
  protected boolean autoGenerateGridBuilder = true;
  protected MyComponentsRepeater<Component> actionButtons;
  protected CsrfTokenHandler csrfTokenHandler;
  public ModalDialog(final String id)
  {
    super(id);
    actionButtons = new MyComponentsRepeater<Component>("actionButtons");
    mainContainer = new WebMarkupContainer("mainContainer");
    add(mainContainer.setOutputMarkupId(true));
    mainContainer.add(mainSubContainer = new WebMarkupContainer("mainSubContainer"));
    gridContentContainer = new WebMarkupContainer("gridContent");
    gridContentContainer.setOutputMarkupId(true);
    buttonBarContainer = new WebMarkupContainer("buttonBar");
    buttonBarContainer.setOutputMarkupId(true);
  }
  @Override
  protected void onInitialize()
  {
    super.onInitialize();
    if (bigWindow == true) {
      mainContainer.add(AttributeModifier.append("class", "big-modal"));
    }
  }
  public ModalDialog setBigWindow()
  {
    bigWindow = true;
    draggable = false;
    return this;
  }
  public ModalDialog setLazyBinding()
  {
    this.lazyBinding = true;
    mainSubContainer.setVisible(false);
    return this;
  }
  public void bind(final AjaxRequestTarget target)
  {
    actionButtons.render();
    mainSubContainer.setVisible(true);
    target.appendJavaScript(getJavaScriptAction());
  }
  public boolean isBound()
  {
    return mainSubContainer.isVisible();
  }
  public ModalDialog setDraggable(final boolean draggable)
  {
    this.draggable = draggable;
    return this;
  }
  public ModalDialog setResizable(final boolean resizable)
  {
    this.resizable = resizable;
    return this;
  }
  public ModalDialog setShowCancelButton()
  {
    this.showCancelButton = true;
    return this;
  }
  public ModalDialog setEscapeKeyEnabled(final boolean escapeKeyEnabled)
  {
    this.escapeKeyEnabled = escapeKeyEnabled;
    return this;
  }
  public ModalDialog setCloseButtonLabel(final String closeButtonLabel)
  {
    this.closeButtonLabel = closeButtonLabel;
    return this;
  }
  public ModalDialog setCloseButtonTooltip(final IModel<String> tooltipTitle, final IModel<String> tooltipContent)
  {
    WicketUtils.addTooltip(this.closeButtonPanel.getButton(), tooltipTitle, tooltipContent);
    return this;
  }
  @SuppressWarnings("serial")
  public ModalDialog wantsNotificationOnClose()
  {
    mainContainer.add(new AjaxEventBehavior("hidden") {
      @Override
      protected void onEvent(final AjaxRequestTarget target)
      {
        csrfTokenHandler.onSubmit();
        handleCloseEvent(target);
      }
    });
    return this;
  }
  public ModalDialog addAjaxEventBehavior(final AjaxEventBehavior behavior)
  {
    mainContainer.add(behavior);
    return this;
  }
  public String getMainContainerMarkupId()
  {
    return mainContainer.getMarkupId(true);
  }
  @Override
  public void renderHead(final IHeaderResponse response)
  {
    super.renderHead(response);
    if (lazyBinding == false) {
      final String script = getJavaScriptAction();
      response.render(OnDomReadyHeaderItem.forScript(script));
    }
  }
  private String getJavaScriptAction()
  {
    final StringBuffer script = new StringBuffer();
    script.append("$('#").append(getMainContainerMarkupId()).append("').modal({keyboard: ").append(escapeKeyEnabled)
    .append(", show: false });");
    final boolean isResizable = (resizable == null && bigWindow == true) || Boolean.TRUE.equals(resizable) == true;
    if (draggable == true || isResizable == true) {
      script.append(" $('#").append(getMainContainerMarkupId()).append("')");
    }
    if (draggable == true) {
      script.append(".draggable()");
    }
    if (isResizable) {
      script.append(".resizable({ alsoResize: '#")
      .append(getMainContainerMarkupId())
      .append(
          ", .modal-body', resize: function( event, ui ) {$('.modal-body').css('max-height', '4000px');}, minWidth: 300, minHeight: 200 })");
    }
    return script.toString();
  }
  public ModalDialog open(final AjaxRequestTarget target)
  {
    target.appendJavaScript("$('#" + getMainContainerMarkupId() + "').modal('show');");
    return this;
  }
  public void close(final AjaxRequestTarget target)
  {
    csrfTokenHandler.onSubmit();
    target.appendJavaScript("$('#" + getMainContainerMarkupId() + "').modal('hide');");
  }
  public ModalDialog addContent(final AjaxRequestTarget target)
  {
    target.add(gridContentContainer);
    return this;
  }
  public ModalDialog addButtonBar(final AjaxRequestTarget target)
  {
    target.add(buttonBarContainer);
    return this;
  }
  public ModalDialog addTitleLabel(final AjaxRequestTarget target)
  {
    target.add(titleLabel);
    return this;
  }
  public abstract void init();
  public ModalDialog setTitle(final String title)
  {
    return setTitle(Model.of(title));
  }
  public ModalDialog setTitle(final IModel<String> title)
  {
    titleContainer = new WebMarkupContainer("titleContainer");
    mainSubContainer.add(titleContainer.setOutputMarkupId(true));
    titleContainer.add(titleLabel = new Label("titleText", title));
    titleLabel.setOutputMarkupId(true);
    return this;
  }
  public ModalDialog clearContent()
  {
    gridContentContainer.removeAll();
    if (autoGenerateGridBuilder == true) {
      gridBuilder = new GridBuilder(gridContentContainer, "flowform");
    }
    initFeedback(gridContentContainer);
    return this;
  }
  @SuppressWarnings("serial")
  protected void init(final Form< ? > form)
  {
    this.form = form;
    csrfTokenHandler = new CsrfTokenHandler(form);
    mainSubContainer.add(form);
    form.add(gridContentContainer);
    form.add(buttonBarContainer);
    if (showCancelButton == true) {
      final SingleButtonPanel cancelButton = appendNewAjaxActionButton(new AjaxCallback() {
        @Override
        public void callback(final AjaxRequestTarget target)
        {
          csrfTokenHandler.onSubmit();
          onCancelButtonSubmit(target);
          close(target);
        }
      }, getString("cancel"), SingleButtonPanel.CANCEL);
      cancelButton.getButton().setDefaultFormProcessing(false);
    }
    closeButtonPanel = appendNewAjaxActionButton(new AjaxFormSubmitCallback() {
      @Override
      public void callback(final AjaxRequestTarget target)
      {
        csrfTokenHandler.onSubmit();
        if (onCloseButtonSubmit(target)) {
          close(target);
        }
      }
      @Override
      public void onError(final AjaxRequestTarget target, final Form< ? > form)
      {
        csrfTokenHandler.onSubmit();
        ModalDialog.this.onError(target, form);
      }
    }, closeButtonLabel != null ? closeButtonLabel : getString("close"), SingleButtonPanel.NORMAL);
    buttonBarContainer.add(actionButtons.getRepeatingView());
    form.setDefaultButton(closeButtonPanel.getButton());
    if (autoGenerateGridBuilder == true) {
      gridBuilder = new GridBuilder(gridContentContainer, "flowform");
    }
    initFeedback(gridContentContainer);
  }
  private void initFeedback(final WebMarkupContainer container)
  {
    if (formFeedback == null) {
      formFeedback = new FeedbackPanel("formFeedback", new ComponentFeedbackMessageFilter(form));
      formFeedback.setOutputMarkupId(true);
      formFeedback.setOutputMarkupPlaceholderTag(true);
    }
    container.add(formFeedback);
  }
  protected void ajaxError(final String error, final AjaxRequestTarget target)
  {
    csrfTokenHandler.onSubmit();
    form.error(error);
    target.add(formFeedback);
  }
  protected void handleCloseEvent(final AjaxRequestTarget target)
  {
    csrfTokenHandler.onSubmit();
  }
  protected void onCancelButtonSubmit(final AjaxRequestTarget target)
  {
  }
  protected boolean onCloseButtonSubmit(final AjaxRequestTarget target)
  {
    return true;
  }
  protected void onError(final AjaxRequestTarget target, final Form< ? > form)
  {
  }
  @Override
  protected void onBeforeRender()
  {
    super.onBeforeRender();
    if (lazyBinding == false) {
      actionButtons.render();
    }
  }
  public String getFormId()
  {
    return "form";
  }
  public SingleButtonPanel appendNewAjaxActionButton(final AjaxCallback ajaxCallback, final String label, final String... classnames)
  {
    final SingleButtonPanel result = addNewAjaxActionButton(ajaxCallback, label, classnames);
    this.actionButtons.add(result);
    return result;
  }
  public SingleButtonPanel prependNewAjaxActionButton(final AjaxCallback ajaxCallback, final String label, final String... classnames)
  {
    return insertNewAjaxActionButton(ajaxCallback, 0, label, classnames);
  }
  public SingleButtonPanel insertNewAjaxActionButton(final AjaxCallback ajaxCallback, final int position, final String label,
      final String... classnames)
  {
    final SingleButtonPanel result = addNewAjaxActionButton(ajaxCallback, label, classnames);
    this.actionButtons.add(position, result);
    return result;
  }
  private SingleButtonPanel addNewAjaxActionButton(final AjaxCallback ajaxCallback, final String label, final String... classnames)
  {
    final AjaxButton button = new AjaxButton("button", form) {
      private static final long serialVersionUID = -5306532706450731336L;
      @Override
      protected void onSubmit(final AjaxRequestTarget target, final Form< ? > form)
      {
        csrfTokenHandler.onSubmit();
        ajaxCallback.callback(target);
      }
      @Override
      protected void onError(final AjaxRequestTarget target, final Form< ? > form)
      {
        if (ajaxCallback instanceof AjaxFormSubmitCallback) {
          ((AjaxFormSubmitCallback) ajaxCallback).onError(target, form);
        }
      }
    };
    final SingleButtonPanel buttonPanel = new SingleButtonPanel(this.actionButtons.newChildId(), button, label, classnames);
    buttonPanel.add(button);
    return buttonPanel;
  }
  public WebMarkupContainer getMainContainer()
  {
    return mainContainer;
  }
}
