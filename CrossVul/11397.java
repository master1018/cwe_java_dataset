
package org.projectforge.web.wicket.autocompletion;
import java.util.ArrayList;
import java.util.List;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptReferenceHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.util.string.Strings;
import org.projectforge.web.core.JsonBuilder;
import org.projectforge.web.wicket.WicketRenderHeadUtils;
public abstract class PFAutoCompleteBehavior<T> extends AbstractDefaultAjaxBehavior
{
  private static final long serialVersionUID = -6532710378025987377L;
  protected PFAutoCompleteSettings settings;
  protected IAutoCompleteRenderer<String> renderer;
  public PFAutoCompleteBehavior(final IAutoCompleteRenderer<String> renderer, final PFAutoCompleteSettings settings)
  {
    this.renderer = renderer;
    this.settings = settings;
  }
  @Override
  public void renderHead(final Component component, final IHeaderResponse response)
  {
    super.renderHead(component, response);
    WicketRenderHeadUtils.renderMainJavaScriptIncludes(response);
    response.render(JavaScriptReferenceHeaderItem.forUrl("scripts/jquery.wicket-autocomplete.js"));
    renderAutocompleteHead(response);
  }
  private void renderAutocompleteHead(final IHeaderResponse response)
  {
    final String id = getComponent().getMarkupId();
    String indicatorId = findIndicatorId();
    if (Strings.isEmpty(indicatorId)) {
      indicatorId = "null";
    } else {
      indicatorId = "'" + indicatorId + "'";
    }
    final StringBuffer buf = new StringBuffer();
    buf.append("var favorite" + id + " = ");
    final List<T> favorites = getFavorites();
    final MyJsonBuilder builder = new MyJsonBuilder();
    if (favorites != null) {
      buf.append(builder.append(favorites).getAsString());
    } else {
      buf.append(builder.append(getRecentUserInputs()).getAsString());
    }
    buf.append(";").append("var z = $(\"#").append(id).append("\");\n").append("z.autocomplete(\"").append(getCallbackUrl()).append("\",{");
    boolean first = true;
    for (final String setting : getSettingsJS()) {
      if (first == true)
        first = false;
      else buf.append(", ");
      buf.append(setting);
    }
    if (first == true)
      first = false;
    else buf.append(", ");
    buf.append("favoriteEntries:favorite" + id);
    buf.append("});");
    if (settings.isHasFocus() == true) {
      buf.append("\nz.focus();");
    }
    final String initJS = buf.toString();
    response.render(OnDomReadyHeaderItem.forScript(initJS));
  }
  protected final List<String> getSettingsJS()
  {
    final List<String> result = new ArrayList<String>();
    addSetting(result, "matchContains", settings.isMatchContains());
    addSetting(result, "minChars", settings.getMinChars());
    addSetting(result, "delay", settings.getDelay());
    addSetting(result, "matchCase", settings.isMatchCase());
    addSetting(result, "matchSubset", settings.isMatchSubset());
    addSetting(result, "cacheLength", settings.getCacheLength());
    addSetting(result, "mustMatch", settings.isMustMatch());
    addSetting(result, "selectFirst", settings.isSelectFirst());
    addSetting(result, "selectOnly", settings.isSelectOnly());
    addSetting(result, "maxItemsToShow", settings.getMaxItemsToShow());
    addSetting(result, "autoFill", settings.isAutoFill());
    addSetting(result, "autoSubmit", settings.isAutoSubmit());
    addSetting(result, "scroll", settings.isScroll());
    addSetting(result, "scrollHeight", settings.getScrollHeight());
    addSetting(result, "width", settings.getWidth());
    addSetting(result, "deletableItem", settings.isDeletableItem());
    if (settings.isLabelValue() == true) {
      addSetting(result, "labelValue", settings.isLabelValue());
    }
    return result;
  }
  private final void addSetting(final List<String> result, final String name, final Boolean value)
  {
    if (value == null) {
      return;
    }
    result.add(name + ":" + ((value == true) ? "1" : "0"));
  }
  private final void addSetting(final List<String> result, final String name, final Integer value)
  {
    if (value == null) {
      return;
    }
    result.add(name + ":" + value);
  }
  @Override
  protected void onBind()
  {
    getComponent().add(new AbstractDefaultAjaxBehavior() {
      private static final long serialVersionUID = 1L;
      @Override
      protected void respond(final AjaxRequestTarget target)
      {
      }
    });
  }
  @Override
  protected void respond(final AjaxRequestTarget target)
  {
    final RequestCycle requestCycle = RequestCycle.get();
    final org.apache.wicket.util.string.StringValue val = requestCycle.getRequest().getQueryParameters().getParameterValue("q");
    onRequest(val != null ? val.toString() : null, requestCycle);
  }
  protected final void onRequest(final String val, final RequestCycle requestCycle)
  {
    final List<T> choices = getChoices(val);
    final MyJsonBuilder builder = new MyJsonBuilder();
    final String json = builder.append(choices).getAsString();
    requestCycle.scheduleRequestHandlerAfterCurrent(new TextRequestHandler("application/json", "utf-8", json));
  }
  protected abstract List<T> getChoices(String input);
  protected abstract List<T> getFavorites();
  protected abstract List<String> getRecentUserInputs();
  protected abstract String formatValue(T value);
  protected String formatLabel(final T value)
  {
    return null;
  }
  private class MyJsonBuilder extends JsonBuilder
  {
    private MyJsonBuilder()
    {
      setEscapeHtml(true);
    }
    @SuppressWarnings("unchecked")
    @Override
    protected String formatValue(final Object obj)
    {
      if (obj instanceof String) {
        return obj.toString();
      } else {
        return PFAutoCompleteBehavior.this.formatValue((T) obj);
      }
    }
    @SuppressWarnings("unchecked")
    @Override
    protected Object transform(final Object obj)
    {
      if (settings.isLabelValue() == true) {
        final Object[] oa = new Object[2];
        if (obj instanceof String) {
          oa[0] = obj;
          oa[1] = obj;
        } else {
          oa[0] = PFAutoCompleteBehavior.this.formatLabel((T) obj);
          oa[1] = PFAutoCompleteBehavior.this.formatValue((T) obj);
        }
        return oa;
      } else {
        return obj;
      }
    }
  };
}
