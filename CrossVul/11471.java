
package org.projectforge.web.admin;
import java.util.TimeZone;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.projectforge.core.Configuration;
import org.projectforge.core.ConfigurationDO;
import org.projectforge.database.InitDatabaseDao;
import org.projectforge.user.UserDao;
import org.projectforge.web.wicket.AbstractForm;
import org.projectforge.web.wicket.CsrfTokenHandler;
import org.projectforge.web.wicket.WicketUtils;
import org.projectforge.web.wicket.bootstrap.GridBuilder;
import org.projectforge.web.wicket.components.MaxLengthTextField;
import org.projectforge.web.wicket.components.RequiredMaxLengthTextField;
import org.projectforge.web.wicket.components.SingleButtonPanel;
import org.projectforge.web.wicket.components.TimeZonePanel;
import org.projectforge.web.wicket.flowlayout.DivPanel;
import org.projectforge.web.wicket.flowlayout.DivType;
import org.projectforge.web.wicket.flowlayout.FieldsetPanel;
import org.projectforge.web.wicket.flowlayout.InputPanel;
import org.projectforge.web.wicket.flowlayout.ParTextPanel;
import org.projectforge.web.wicket.flowlayout.PasswordPanel;
import org.projectforge.web.wicket.flowlayout.RadioGroupPanel;
public class SetupForm extends AbstractForm<SetupForm, SetupPage>
{
  private static final long serialVersionUID = -277853572580468505L;
  private static final String MAGIC_PASSWORD = "******";
  @SpringBean(name = "userDao")
  private UserDao userDao;
  private final SetupTarget setupMode = SetupTarget.TEST_DATA;
  private final TimeZone timeZone = TimeZone.getDefault();
  private String sysopEMail;
  private String feedbackEMail;
  private String calendarDomain;
  private final String adminUsername = InitDatabaseDao.DEFAULT_ADMIN_USER;
  @SuppressWarnings("unused")
  private String password;
  @SuppressWarnings("unused")
  private String passwordRepeat;
  private String encryptedPassword;
  private final CsrfTokenHandler csrfTokenHandler;
  public SetupForm(final SetupPage parentPage)
  {
    super(parentPage, "setupform");
    csrfTokenHandler = new CsrfTokenHandler(this);
  }
  @Override
  @SuppressWarnings("serial")
  protected void init()
  {
    add(createFeedbackPanel());
    final GridBuilder gridBuilder = newGridBuilder(this, "flowform");
    gridBuilder.newFormHeading(getString("administration.setup.heading"));
    final DivPanel panel = gridBuilder.getPanel();
    panel.add(new ParTextPanel(panel.newChildId(), getString("administration.setup.heading.subtitle")));
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.setup.target"));
      final DivPanel radioPanel = new DivPanel(fs.newChildId(), DivType.RADIOBOX);
      fs.add(radioPanel);
      fs.setLabelFor(radioPanel);
      final RadioGroupPanel<SetupTarget> radioGroup = new RadioGroupPanel<SetupTarget>(radioPanel.newChildId(), "setuptarget",
          new PropertyModel<SetupTarget>(this, "setupMode"));
      radioPanel.add(radioGroup);
      for (final SetupTarget target : SetupTarget.values()) {
        radioGroup.add(new Model<SetupTarget>(target), getString(target.getI18nKey()), getString(target.getI18nKey() + ".tooltip"));
      }
    }
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("username"));
      fs.add(new RequiredMaxLengthTextField(InputPanel.WICKET_ID, new PropertyModel<String>(this, "adminUsername"), 100));
    }
    final PasswordTextField passwordField = new PasswordTextField(PasswordPanel.WICKET_ID, new PropertyModel<String>(this, "password")) {
      @Override
      protected void onComponentTag(final ComponentTag tag)
      {
        super.onComponentTag(tag);
        if (encryptedPassword == null) {
          tag.put("value", "");
        } else if (StringUtils.isEmpty(getConvertedInput()) == false) {
          tag.put("value", MAGIC_PASSWORD);
        }
      }
    };
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("password"));
      passwordField.setRequired(true); 
      fs.add(passwordField);
      WicketUtils.setFocus(passwordField);
    }
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("passwordRepeat"));
      final PasswordTextField passwordRepeatField = new PasswordTextField(PasswordPanel.WICKET_ID, new PropertyModel<String>(this,
          "passwordRepeat")) {
        @Override
        protected void onComponentTag(final ComponentTag tag)
        {
          super.onComponentTag(tag);
          if (encryptedPassword == null) {
            tag.put("value", "");
          } else if (StringUtils.isEmpty(getConvertedInput()) == false) {
            tag.put("value", MAGIC_PASSWORD);
          }
        }
      };
      passwordRepeatField.setRequired(true); 
      passwordRepeatField.add(new IValidator<String>() {
        @Override
        public void validate(final IValidatable<String> validatable)
        {
          final String input = validatable.getValue();
          final String passwordInput = passwordField.getConvertedInput();
          if (StringUtils.equals(input, passwordInput) == false) {
            passwordRepeatField.error(getString("user.error.passwordAndRepeatDoesNotMatch"));
            encryptedPassword = null;
            return;
          }
          if (MAGIC_PASSWORD.equals(passwordInput) == false || encryptedPassword == null) {
            final String errorMsgKey = userDao.checkPasswordQuality(passwordInput);
            if (errorMsgKey != null) {
              encryptedPassword = null;
              passwordField.error(getString(errorMsgKey));
            } else {
              encryptedPassword = userDao.encryptPassword(passwordInput);
            }
          }
        }
      });
      fs.add(passwordRepeatField);
    }
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.configuration.param.timezone"));
      final TimeZonePanel timeZone = new TimeZonePanel(fs.newChildId(), new PropertyModel<TimeZone>(this, "timeZone"));
      fs.setLabelFor(timeZone);
      fs.add(timeZone);
      fs.addHelpIcon(getString("administration.configuration.param.timezone.description"));
    }
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.configuration.param.calendarDomain"));
      final RequiredMaxLengthTextField textField = new RequiredMaxLengthTextField(InputPanel.WICKET_ID, new PropertyModel<String>(this,
          "calendarDomain"), ConfigurationDO.PARAM_LENGTH);
      fs.add(textField);
      textField.add(new IValidator<String>() {
        @Override
        public void validate(final IValidatable<String> validatable)
        {
          if (Configuration.isDomainValid(validatable.getValue()) == false) {
            textField.error(getString("validation.error.generic"));
          }
        }
      });
      fs.addHelpIcon(getString("administration.configuration.param.calendarDomain.description"));
    }
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.configuration.param.systemAdministratorEMail.label"),
          getString("email"));
      fs.add(new MaxLengthTextField(InputPanel.WICKET_ID, new PropertyModel<String>(this, "sysopEMail"), ConfigurationDO.PARAM_LENGTH));
      fs.addHelpIcon(getString("administration.configuration.param.systemAdministratorEMail.description"));
    }
    {
      final FieldsetPanel fs = gridBuilder.newFieldset(getString("administration.configuration.param.feedbackEMail.label"),
          getString("email"));
      fs.add(new MaxLengthTextField(InputPanel.WICKET_ID, new PropertyModel<String>(this, "feedbackEMail"), ConfigurationDO.PARAM_LENGTH));
      fs.addHelpIcon(getString("administration.configuration.param.feedbackEMail.description"));
    }
    final RepeatingView actionButtons = new RepeatingView("buttons");
    add(actionButtons);
    {
      final Button finishButton = new Button(SingleButtonPanel.WICKET_ID, new Model<String>("finish")) {
        @Override
        public final void onSubmit()
        {
          csrfTokenHandler.onSubmit();
          parentPage.finishSetup();
        }
      };
      final SingleButtonPanel finishButtonPanel = new SingleButtonPanel(actionButtons.newChildId(), finishButton,
          getString("administration.setup.finish"), SingleButtonPanel.DEFAULT_SUBMIT);
      actionButtons.add(finishButtonPanel);
      setDefaultButton(finishButton);
    }
  }
  @Override
  protected void onSubmit()
  {
    super.onSubmit();
    csrfTokenHandler.onSubmit();
  }
  public SetupTarget getSetupMode()
  {
    return setupMode;
  }
  public TimeZone getTimeZone()
  {
    return timeZone;
  }
  public String getCalendarDomain()
  {
    return calendarDomain;
  }
  public String getSysopEMail()
  {
    return sysopEMail;
  }
  public String getFeedbackEMail()
  {
    return feedbackEMail;
  }
  public String getEncryptedPassword()
  {
    return encryptedPassword;
  }
  public String getAdminUsername()
  {
    return adminUsername;
  }
}
