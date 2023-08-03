
package org.projectforge.web.core;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.projectforge.access.AccessChecker;
import org.projectforge.user.PFUserContext;
import org.projectforge.user.UserRights;
import org.projectforge.user.UserXmlPreferencesCache;
import org.projectforge.web.FavoritesMenu;
import org.projectforge.web.LayoutSettingsPage;
import org.projectforge.web.LoginPage;
import org.projectforge.web.MenuEntry;
import org.projectforge.web.core.menuconfig.MenuConfig;
import org.projectforge.web.dialog.ModalDialog;
import org.projectforge.web.doc.DocumentationPage;
import org.projectforge.web.mobile.MenuMobilePage;
import org.projectforge.web.user.ChangePasswordPage;
import org.projectforge.web.user.MyAccountEditPage;
import org.projectforge.web.wicket.AbstractSecuredPage;
import org.projectforge.web.wicket.CsrfTokenHandler;
import org.projectforge.web.wicket.FeedbackPage;
import org.projectforge.web.wicket.MySession;
import org.projectforge.web.wicket.WicketUtils;
import org.projectforge.web.wicket.flowlayout.FieldsetPanel;
public class NavTopPanel extends NavAbstractPanel
{
  private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NavTopPanel.class);
  private static final long serialVersionUID = -7858806882044188339L;
  private FavoritesMenu favoritesMenu;
  private final AccessChecker accessChecker;
  private final UserXmlPreferencesCache userXmlPreferencesCache;
  private BookmarkDialog bookmarkDialog;
  private CsrfTokenHandler csrfTokenHandler;
  public NavTopPanel(final String id, final UserXmlPreferencesCache userXmlPreferencesCache, final AccessChecker accessChecker)
  {
    super(id);
    this.userXmlPreferencesCache = userXmlPreferencesCache;
    this.accessChecker = accessChecker;
  }
  public void init(final AbstractSecuredPage page)
  {
    getMenu();
    this.favoritesMenu = FavoritesMenu.get();
    final WebMarkupContainer goMobile = new WebMarkupContainer("goMobile");
    add(goMobile);
    if (page.getMySession().isMobileUserAgent() == true) {
      goMobile.add(new BookmarkablePageLink<Void>("link", MenuMobilePage.class));
    } else {
      goMobile.setVisible(false);
    }
    final BookmarkablePageLink<Void> layoutSettingsMenuLink = new BookmarkablePageLink<Void>("layoutSettingsMenuLink",
        LayoutSettingsPage.class);
    if (UserRights.getAccessChecker().isRestrictedUser() == true) {
      layoutSettingsMenuLink.setVisible(false);
    }
    add(new MenuConfig("menuconfig", getMenu(), favoritesMenu));
    @SuppressWarnings("serial")
    final Form<String> searchForm = new Form<String>("searchForm") {
      private String searchString;
      @Override
      protected void onSubmit()
      {
        csrfTokenHandler.onSubmit();
        if (StringUtils.isNotBlank(searchString) == true) {
          final SearchPage searchPage = new SearchPage(new PageParameters(), searchString);
          setResponsePage(searchPage);
        }
        super.onSubmit();
      }
    };
    csrfTokenHandler = new CsrfTokenHandler(searchForm);
    add(searchForm);
    final TextField<String> searchField = new TextField<String>("searchField", new PropertyModel<String>(searchForm, "searchString"));
    WicketUtils.setPlaceHolderAttribute(searchField, getString("search.search"));
    searchForm.add(searchField);
    add(layoutSettingsMenuLink);
    add(new BookmarkablePageLink<Void>("feedbackLink", FeedbackPage.class));
    {
      @SuppressWarnings("serial")
      final AjaxLink<Void> showBookmarkLink = new AjaxLink<Void>("showBookmarkLink") {
        @Override
        public void onClick(final AjaxRequestTarget target)
        {
          bookmarkDialog.open(target);
          bookmarkDialog.redraw().addContent(target);
        }
      };
      add(showBookmarkLink);
      addBookmarkDialog();
    }
    {
      add(new Label("user", PFUserContext.getUser().getFullname()));
      if (accessChecker.isRestrictedUser() == true) {
        final BookmarkablePageLink<Void> changePasswordLink = new BookmarkablePageLink<Void>("myAccountLink", ChangePasswordPage.class);
        add(changePasswordLink);
      } else {
        final BookmarkablePageLink<Void> myAccountLink = new BookmarkablePageLink<Void>("myAccountLink", MyAccountEditPage.class);
        add(myAccountLink);
      }
      final BookmarkablePageLink<Void> documentationLink = new BookmarkablePageLink<Void>("documentationLink", DocumentationPage.class);
      add(documentationLink);
      @SuppressWarnings("serial")
      final Link<String> logoutLink = new Link<String>("logoutLink") {
        @Override
        public void onClick()
        {
          LoginPage.logout((MySession) getSession(), (WebRequest) getRequest(), (WebResponse) getResponse(), userXmlPreferencesCache);
          setResponsePage(LoginPage.class);
        };
      };
      add(logoutLink);
    }
    addCompleteMenu();
    addFavoriteMenu();
  }
  @SuppressWarnings("serial")
  private void addCompleteMenu()
  {
    final Label totalMenuSuffixLabel = new MenuSuffixLabel("totalMenuCounter", new Model<Integer>() {
      @Override
      public Integer getObject()
      {
        int counter = 0;
        if (menu.getMenuEntries() == null) {
          return counter;
        }
        for (final MenuEntry menuEntry : menu.getMenuEntries()) {
          final IModel<Integer> newCounterModel = menuEntry.getNewCounterModel();
          if (newCounterModel != null && newCounterModel.getObject() != null) {
            counter += newCounterModel.getObject();
          }
        }
        return counter;
      };
    });
    add(totalMenuSuffixLabel);
    final RepeatingView completeMenuCategoryRepeater = new RepeatingView("completeMenuCategoryRepeater");
    add(completeMenuCategoryRepeater);
    if (menu.getMenuEntries() != null) {
      for (final MenuEntry menuEntry : menu.getMenuEntries()) {
        if (menuEntry.getSubMenuEntries() == null) {
          continue;
        }
        final WebMarkupContainer categoryContainer = new WebMarkupContainer(completeMenuCategoryRepeater.newChildId());
        completeMenuCategoryRepeater.add(categoryContainer);
        categoryContainer.add(new Label("menuCategoryLabel", getString(menuEntry.getI18nKey())));
        final Label areaSuffixLabel = getSuffixLabel(menuEntry);
        categoryContainer.add(areaSuffixLabel);
        if (menuEntry.hasSubMenuEntries() == false) {
          continue;
        }
        final RepeatingView completeSubMenuRepeater = new RepeatingView("completeSubMenuRepeater");
        categoryContainer.add(completeSubMenuRepeater);
        for (final MenuEntry subMenuEntry : menuEntry.getSubMenuEntries()) {
          if (subMenuEntry.getSubMenuEntries() != null) {
            log.error("Oups: sub sub menus not supported: " + menuEntry.getId() + " has child menus which are ignored.");
          }
          final WebMarkupContainer subMenuItem = new WebMarkupContainer(completeSubMenuRepeater.newChildId());
          completeSubMenuRepeater.add(subMenuItem);
          final AbstractLink link = getMenuEntryLink(subMenuEntry, true);
          if (link != null) {
            subMenuItem.add(link);
          } else {
            subMenuItem.setVisible(false);
          }
        }
      }
    }
  }
  private void addFavoriteMenu()
  {
    final RepeatingView menuRepeater = new RepeatingView("menuRepeater");
    add(menuRepeater);
    final Collection<MenuEntry> menuEntries = favoritesMenu.getMenuEntries();
    if (menuEntries != null) {
      for (final MenuEntry menuEntry : menuEntries) {
        final WebMarkupContainer menuItem = new WebMarkupContainer(menuRepeater.newChildId());
        menuRepeater.add(menuItem);
        final AbstractLink link = getMenuEntryLink(menuEntry, true);
        if (link == null) {
          menuItem.setVisible(false);
          continue;
        }
        menuItem.add(link);
        final WebMarkupContainer subMenuContainer = new WebMarkupContainer("subMenu");
        menuItem.add(subMenuContainer);
        final WebMarkupContainer caret = new WebMarkupContainer("caret");
        link.add(caret);
        if (menuEntry.hasSubMenuEntries() == false) {
          subMenuContainer.setVisible(false);
          caret.setVisible(false);
          continue;
        }
        menuItem.add(AttributeModifier.append("class", "dropdown"));
        link.add(AttributeModifier.append("class", "dropdown-toggle"));
        link.add(AttributeModifier.append("data-toggle", "dropdown"));
        final RepeatingView subMenuRepeater = new RepeatingView("subMenuRepeater");
        subMenuContainer.add(subMenuRepeater);
        for (final MenuEntry subMenuEntry : menuEntry.getSubMenuEntries()) {
          if (subMenuEntry.hasSubMenuEntries() == false) {
            final WebMarkupContainer subMenuItem = new WebMarkupContainer(subMenuRepeater.newChildId());
            subMenuRepeater.add(subMenuItem);
            final AbstractLink subLink = getMenuEntryLink(subMenuEntry, true);
            if (subLink == null) {
              subMenuItem.setVisible(false);
              continue;
            }
            subMenuItem.add(subLink);
            continue;
          }
          for (final MenuEntry subsubMenuEntry : subMenuEntry.getSubMenuEntries()) {
            final WebMarkupContainer subMenuItem = new WebMarkupContainer(subMenuRepeater.newChildId());
            subMenuRepeater.add(subMenuItem);
            final AbstractLink subLink = getMenuEntryLink(subsubMenuEntry, true);
            if (subLink == null) {
              subMenuItem.setVisible(false);
              continue;
            }
            subMenuItem.add(subLink);
          }
        }
      }
    }
  }
  private void addBookmarkDialog()
  {
    final AbstractSecuredPage parentPage = (AbstractSecuredPage) getPage();
    bookmarkDialog = new BookmarkDialog(parentPage.newModalDialogId());
    bookmarkDialog.setOutputMarkupId(true);
    parentPage.add(bookmarkDialog);
    bookmarkDialog.init();
  }
  @SuppressWarnings("serial")
  private class BookmarkDialog extends ModalDialog
  {
    public BookmarkDialog(final String id)
    {
      super(id);
    }
    @Override
    public void init()
    {
      setTitle(getString("bookmark.title"));
      init(new Form<String>(getFormId()));
      gridBuilder.newFormHeading(""); 
    }
    private BookmarkDialog redraw()
    {
      clearContent();
      final AbstractSecuredPage page = (AbstractSecuredPage) NavTopPanel.this.getPage();
      {
        final FieldsetPanel fs = gridBuilder.newFieldset(getString("bookmark.directPageLink")).setLabelSide(false);
        final TextArea<String> textArea = new TextArea<String>(fs.getTextAreaId(), new Model<String>(page.getPageAsLink()));
        fs.add(textArea);
        textArea.add(AttributeModifier.replace("onClick", "$(this).select();"));
      }
      final PageParameters params = page.getBookmarkableInitialParameters();
      if (params.isEmpty() == false) {
        final FieldsetPanel fs = gridBuilder.newFieldset(getString(page.getTitleKey4BookmarkableInitialParameters())).setLabelSide(false);
        final TextArea<String> textArea = new TextArea<String>(fs.getTextAreaId(), new Model<String>(page.getPageAsLink(params)));
        fs.add(textArea);
        textArea.add(AttributeModifier.replace("onClick", "$(this).select();"));
      }
      return this;
    }
  }
}
