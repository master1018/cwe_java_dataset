package eu.siacs.conversations.ui;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v13.view.inputmethod.InputConnectionCompat;
import android.support.v13.view.inputmethod.InputContentInfoCompat;
import android.text.Editable;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import eu.siacs.conversations.Config;
import eu.siacs.conversations.R;
import eu.siacs.conversations.crypto.axolotl.AxolotlService;
import eu.siacs.conversations.crypto.axolotl.FingerprintStatus;
import eu.siacs.conversations.databinding.FragmentConversationBinding;
import eu.siacs.conversations.entities.Account;
import eu.siacs.conversations.entities.Blockable;
import eu.siacs.conversations.entities.Contact;
import eu.siacs.conversations.entities.Conversation;
import eu.siacs.conversations.entities.Conversational;
import eu.siacs.conversations.entities.DownloadableFile;
import eu.siacs.conversations.entities.Message;
import eu.siacs.conversations.entities.MucOptions;
import eu.siacs.conversations.entities.MucOptions.User;
import eu.siacs.conversations.entities.Presence;
import eu.siacs.conversations.entities.ReadByMarker;
import eu.siacs.conversations.entities.Transferable;
import eu.siacs.conversations.entities.TransferablePlaceholder;
import eu.siacs.conversations.http.HttpDownloadConnection;
import eu.siacs.conversations.persistance.FileBackend;
import eu.siacs.conversations.services.MessageArchiveService;
import eu.siacs.conversations.services.XmppConnectionService;
import eu.siacs.conversations.ui.adapter.MediaPreviewAdapter;
import eu.siacs.conversations.ui.adapter.MessageAdapter;
import eu.siacs.conversations.ui.util.ActivityResult;
import eu.siacs.conversations.ui.util.Attachment;
import eu.siacs.conversations.ui.util.ConversationMenuConfigurator;
import eu.siacs.conversations.ui.util.DateSeparator;
import eu.siacs.conversations.ui.util.EditMessageActionModeCallback;
import eu.siacs.conversations.ui.util.ListViewUtils;
import eu.siacs.conversations.ui.util.MenuDoubleTabUtil;
import eu.siacs.conversations.ui.util.MucDetailsContextMenuHelper;
import eu.siacs.conversations.ui.util.PendingItem;
import eu.siacs.conversations.ui.util.PresenceSelector;
import eu.siacs.conversations.ui.util.ScrollState;
import eu.siacs.conversations.ui.util.SendButtonAction;
import eu.siacs.conversations.ui.util.SendButtonTool;
import eu.siacs.conversations.ui.util.ShareUtil;
import eu.siacs.conversations.ui.widget.EditMessage;
import eu.siacs.conversations.utils.GeoHelper;
import eu.siacs.conversations.utils.MessageUtils;
import eu.siacs.conversations.utils.NickValidityChecker;
import eu.siacs.conversations.utils.Patterns;
import eu.siacs.conversations.utils.QuickLoader;
import eu.siacs.conversations.utils.StylingHelper;
import eu.siacs.conversations.utils.TimeframeUtils;
import eu.siacs.conversations.utils.UIHelper;
import eu.siacs.conversations.xmpp.XmppConnection;
import eu.siacs.conversations.xmpp.chatstate.ChatState;
import eu.siacs.conversations.xmpp.jingle.JingleConnection;
import rocks.xmpp.addr.Jid;
import static eu.siacs.conversations.ui.XmppActivity.EXTRA_ACCOUNT;
import static eu.siacs.conversations.ui.XmppActivity.REQUEST_INVITE_TO_CONVERSATION;
import static eu.siacs.conversations.ui.util.SoftKeyboardUtils.hideSoftKeyboard;
public class ConversationFragment extends XmppFragment implements EditMessage.KeyboardListener {
    public static final int REQUEST_SEND_MESSAGE = 0x0201;
    public static final int REQUEST_DECRYPT_PGP = 0x0202;
    public static final int REQUEST_ENCRYPT_MESSAGE = 0x0207;
    public static final int REQUEST_TRUST_KEYS_TEXT = 0x0208;
    public static final int REQUEST_TRUST_KEYS_ATTACHMENTS = 0x0209;
    public static final int REQUEST_START_DOWNLOAD = 0x0210;
    public static final int REQUEST_ADD_EDITOR_CONTENT = 0x0211;
    public static final int ATTACHMENT_CHOICE_CHOOSE_IMAGE = 0x0301;
    public static final int ATTACHMENT_CHOICE_TAKE_PHOTO = 0x0302;
    public static final int ATTACHMENT_CHOICE_CHOOSE_FILE = 0x0303;
    public static final int ATTACHMENT_CHOICE_RECORD_VOICE = 0x0304;
    public static final int ATTACHMENT_CHOICE_LOCATION = 0x0305;
    public static final int ATTACHMENT_CHOICE_INVALID = 0x0306;
    public static final int ATTACHMENT_CHOICE_RECORD_VIDEO = 0x0307;
    public static final String RECENTLY_USED_QUICK_ACTION = "recently_used_quick_action";
    public static final String STATE_CONVERSATION_UUID = ConversationFragment.class.getName() + ".uuid";
    public static final String STATE_SCROLL_POSITION = ConversationFragment.class.getName() + ".scroll_position";
    public static final String STATE_PHOTO_URI = ConversationFragment.class.getName() + ".media_previews";
    public static final String STATE_MEDIA_PREVIEWS = ConversationFragment.class.getName() + ".take_photo_uri";
    private static final String STATE_LAST_MESSAGE_UUID = "state_last_message_uuid";
    private final List<Message> messageList = new ArrayList<>();
    private final PendingItem<ActivityResult> postponedActivityResult = new PendingItem<>();
    private final PendingItem<String> pendingConversationsUuid = new PendingItem<>();
    private final PendingItem<ArrayList<Attachment>> pendingMediaPreviews = new PendingItem<>();
    private final PendingItem<Bundle> pendingExtras = new PendingItem<>();
    private final PendingItem<Uri> pendingTakePhotoUri = new PendingItem<>();
    private final PendingItem<ScrollState> pendingScrollState = new PendingItem<>();
    private final PendingItem<String> pendingLastMessageUuid = new PendingItem<>();
    private final PendingItem<Message> pendingMessage = new PendingItem<>();
    public Uri mPendingEditorContent = null;
    protected MessageAdapter messageListAdapter;
    private MediaPreviewAdapter mediaPreviewAdapter;
    private String lastMessageUuid = null;
    private Conversation conversation;
    private FragmentConversationBinding binding;
    private Toast messageLoaderToast;
    private ConversationsActivity activity;
    private boolean reInitRequiredOnStart = true;
    private OnClickListener clickToMuc = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ConferenceDetailsActivity.class);
            intent.setAction(ConferenceDetailsActivity.ACTION_VIEW_MUC);
            intent.putExtra("uuid", conversation.getUuid());
            startActivity(intent);
        }
    };
    private OnClickListener leaveMuc = new OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.xmppConnectionService.archiveConversation(conversation);
        }
    };
    private OnClickListener joinMuc = new OnClickListener() {
        @Override
        public void onClick(View v) {
            activity.xmppConnectionService.joinMuc(conversation);
        }
    };
    private OnClickListener enterPassword = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MucOptions muc = conversation.getMucOptions();
            String password = muc.getPassword();
            if (password == null) {
                password = "";
            }
            activity.quickPasswordEdit(password, value -> {
                activity.xmppConnectionService.providePasswordForMuc(conversation, value);
                return null;
            });
        }
    };
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (AbsListView.OnScrollListener.SCROLL_STATE_IDLE == scrollState) {
                fireReadEvent();
            }
        }
        @Override
        public void onScroll(final AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            toggleScrollDownButton(view);
            synchronized (ConversationFragment.this.messageList) {
                if (firstVisibleItem < 5 && conversation != null && conversation.messagesLoaded.compareAndSet(true, false) && messageList.size() > 0) {
                    long timestamp;
                    if (messageList.get(0).getType() == Message.TYPE_STATUS && messageList.size() >= 2) {
                        timestamp = messageList.get(1).getTimeSent();
                    } else {
                        timestamp = messageList.get(0).getTimeSent();
                    }
                    activity.xmppConnectionService.loadMoreMessages(conversation, timestamp, new XmppConnectionService.OnMoreMessagesLoaded() {
                        @Override
                        public void onMoreMessagesLoaded(final int c, final Conversation conversation) {
                            if (ConversationFragment.this.conversation != conversation) {
                                conversation.messagesLoaded.set(true);
                                return;
                            }
                            runOnUiThread(() -> {
                                synchronized (messageList) {
                                    final int oldPosition = binding.messagesView.getFirstVisiblePosition();
                                    Message message = null;
                                    int childPos;
                                    for (childPos = 0; childPos + oldPosition < messageList.size(); ++childPos) {
                                        message = messageList.get(oldPosition + childPos);
                                        if (message.getType() != Message.TYPE_STATUS) {
                                            break;
                                        }
                                    }
                                    final String uuid = message != null ? message.getUuid() : null;
                                    View v = binding.messagesView.getChildAt(childPos);
                                    final int pxOffset = (v == null) ? 0 : v.getTop();
                                    ConversationFragment.this.conversation.populateWithMessages(ConversationFragment.this.messageList);
                                    try {
                                        updateStatusMessages();
                                    } catch (IllegalStateException e) {
                                        Log.d(Config.LOGTAG, "caught illegal state exception while updating status messages");
                                    }
                                    messageListAdapter.notifyDataSetChanged();
                                    int pos = Math.max(getIndexOf(uuid, messageList), 0);
                                    binding.messagesView.setSelectionFromTop(pos, pxOffset);
                                    if (messageLoaderToast != null) {
                                        messageLoaderToast.cancel();
                                    }
                                    conversation.messagesLoaded.set(true);
                                }
                            });
                        }
                        @Override
                        public void informUser(final int resId) {
                            runOnUiThread(() -> {
                                if (messageLoaderToast != null) {
                                    messageLoaderToast.cancel();
                                }
                                if (ConversationFragment.this.conversation != conversation) {
                                    return;
                                }
                                messageLoaderToast = Toast.makeText(view.getContext(), resId, Toast.LENGTH_LONG);
                                messageLoaderToast.show();
                            });
                        }
                    });
                }
            }
        }
    };
    private EditMessage.OnCommitContentListener mEditorContentListener = new EditMessage.OnCommitContentListener() {
        @Override
        public boolean onCommitContent(InputContentInfoCompat inputContentInfo, int flags, Bundle opts, String[] contentMimeTypes) {
            if ((flags & InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                try {
                    inputContentInfo.requestPermission();
                } catch (Exception e) {
                    Log.e(Config.LOGTAG, "InputContentInfoCompat#requestPermission() failed.", e);
                    Toast.makeText(getActivity(), activity.getString(R.string.no_permission_to_access_x, inputContentInfo.getDescription()), Toast.LENGTH_LONG
                    ).show();
                    return false;
                }
            }
            if (hasPermissions(REQUEST_ADD_EDITOR_CONTENT, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                attachEditorContentToConversation(inputContentInfo.getContentUri());
            } else {
                mPendingEditorContent = inputContentInfo.getContentUri();
            }
            return true;
        }
    };
    private Message selectedMessage;
    private OnClickListener mEnableAccountListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Account account = conversation == null ? null : conversation.getAccount();
            if (account != null) {
                account.setOption(Account.OPTION_DISABLED, false);
                activity.xmppConnectionService.updateAccount(account);
            }
        }
    };
    private OnClickListener mUnblockClickListener = new OnClickListener() {
        @Override
        public void onClick(final View v) {
            v.post(() -> v.setVisibility(View.INVISIBLE));
            if (conversation.isDomainBlocked()) {
                BlockContactDialog.show(activity, conversation);
            } else {
                unblockConversation(conversation);
            }
        }
    };
    private OnClickListener mBlockClickListener = this::showBlockSubmenu;
    private OnClickListener mAddBackClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Contact contact = conversation == null ? null : conversation.getContact();
            if (contact != null) {
                activity.xmppConnectionService.createContact(contact, true);
                activity.switchToContactDetails(contact);
            }
        }
    };
    private View.OnLongClickListener mLongPressBlockListener = this::showBlockSubmenu;
    private OnClickListener mAllowPresenceSubscription = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final Contact contact = conversation == null ? null : conversation.getContact();
            if (contact != null) {
                activity.xmppConnectionService.sendPresencePacket(contact.getAccount(),
                        activity.xmppConnectionService.getPresenceGenerator()
                                .sendPresenceUpdatesTo(contact));
                hideSnackbar();
            }
        }
    };
    protected OnClickListener clickToDecryptListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            PendingIntent pendingIntent = conversation.getAccount().getPgpDecryptionService().getPendingIntent();
            if (pendingIntent != null) {
                try {
                    getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(),
                            REQUEST_DECRYPT_PGP,
                            null,
                            0,
                            0,
                            0);
                } catch (SendIntentException e) {
                    Toast.makeText(getActivity(), R.string.unable_to_connect_to_keychain, Toast.LENGTH_SHORT).show();
                    conversation.getAccount().getPgpDecryptionService().continueDecryption(true);
                }
            }
            updateSnackBar(conversation);
        }
    };
    private AtomicBoolean mSendingPgpMessage = new AtomicBoolean(false);
    private OnEditorActionListener mEditorActionListener = (v, actionId, event) -> {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null && imm.isFullscreenMode()) {
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            sendMessage();
            return true;
        } else {
            return false;
        }
    };
    private OnClickListener mScrollButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            stopScrolling();
            setSelection(binding.messagesView.getCount() - 1, true);
        }
    };
    private OnClickListener mSendButtonListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag instanceof SendButtonAction) {
                SendButtonAction action = (SendButtonAction) tag;
                switch (action) {
                    case TAKE_PHOTO:
                    case RECORD_VIDEO:
                    case SEND_LOCATION:
                    case RECORD_VOICE:
                    case CHOOSE_PICTURE:
                        attachFile(action.toChoice());
                        break;
                    case CANCEL:
                        if (conversation != null) {
                            if (conversation.setCorrectingMessage(null)) {
                                binding.textinput.setText("");
                                binding.textinput.append(conversation.getDraftMessage());
                                conversation.setDraftMessage(null);
                            } else if (conversation.getMode() == Conversation.MODE_MULTI) {
                                conversation.setNextCounterpart(null);
                            }
                            updateChatMsgHint();
                            updateSendButton();
                            updateEditablity();
                        }
                        break;
                    default:
                        sendMessage();
                }
            } else {
                sendMessage();
            }
        }
    };
    private int completionIndex = 0;
    private int lastCompletionLength = 0;
    private String incomplete;
    private int lastCompletionCursor;
    private boolean firstWord = false;
    private Message mPendingDownloadableMessage;
    private static ConversationFragment findConversationFragment(Activity activity) {
        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.main_fragment);
        if (fragment != null && fragment instanceof ConversationFragment) {
            return (ConversationFragment) fragment;
        }
        fragment = activity.getFragmentManager().findFragmentById(R.id.secondary_fragment);
        if (fragment != null && fragment instanceof ConversationFragment) {
            return (ConversationFragment) fragment;
        }
        return null;
    }
    public static void startStopPending(Activity activity) {
        ConversationFragment fragment = findConversationFragment(activity);
        if (fragment != null) {
            fragment.messageListAdapter.startStopPending();
        }
    }
    public static void downloadFile(Activity activity, Message message) {
        ConversationFragment fragment = findConversationFragment(activity);
        if (fragment != null) {
            fragment.startDownloadable(message);
        }
    }
    public static void registerPendingMessage(Activity activity, Message message) {
        ConversationFragment fragment = findConversationFragment(activity);
        if (fragment != null) {
            fragment.pendingMessage.push(message);
        }
    }
    public static void openPendingMessage(Activity activity) {
        ConversationFragment fragment = findConversationFragment(activity);
        if (fragment != null) {
            Message message = fragment.pendingMessage.pop();
            if (message != null) {
                fragment.messageListAdapter.openDownloadable(message);
            }
        }
    }
    public static Conversation getConversation(Activity activity) {
        return getConversation(activity, R.id.secondary_fragment);
    }
    private static Conversation getConversation(Activity activity, @IdRes int res) {
        final Fragment fragment = activity.getFragmentManager().findFragmentById(res);
        if (fragment != null && fragment instanceof ConversationFragment) {
            return ((ConversationFragment) fragment).getConversation();
        } else {
            return null;
        }
    }
    public static ConversationFragment get(Activity activity) {
        FragmentManager fragmentManager = activity.getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_fragment);
        if (fragment != null && fragment instanceof ConversationFragment) {
            return (ConversationFragment) fragment;
        } else {
            fragment = fragmentManager.findFragmentById(R.id.secondary_fragment);
            return fragment != null && fragment instanceof ConversationFragment ? (ConversationFragment) fragment : null;
        }
    }
    public static Conversation getConversationReliable(Activity activity) {
        final Conversation conversation = getConversation(activity, R.id.secondary_fragment);
        if (conversation != null) {
            return conversation;
        }
        return getConversation(activity, R.id.main_fragment);
    }
    private static boolean allGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    private static boolean writeGranted(int[] grantResults, String[] permission) {
        for (int i = 0; i < grantResults.length; ++i) {
            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }
    private static String getFirstDenied(int[] grantResults, String[] permissions) {
        for (int i = 0; i < grantResults.length; ++i) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                return permissions[i];
            }
        }
        return null;
    }
    private static boolean scrolledToBottom(AbsListView listView) {
        final int count = listView.getCount();
        if (count == 0) {
            return true;
        } else if (listView.getLastVisiblePosition() == count - 1) {
            final View lastChild = listView.getChildAt(listView.getChildCount() - 1);
            return lastChild != null && lastChild.getBottom() <= listView.getHeight();
        } else {
            return false;
        }
    }
    private void toggleScrollDownButton() {
        toggleScrollDownButton(binding.messagesView);
    }
    private void toggleScrollDownButton(AbsListView listView) {
        if (conversation == null) {
            return;
        }
        if (scrolledToBottom(listView)) {
            lastMessageUuid = null;
            hideUnreadMessagesCount();
        } else {
            binding.scrollToBottomButton.setEnabled(true);
            binding.scrollToBottomButton.show();
            if (lastMessageUuid == null) {
                lastMessageUuid = conversation.getLatestMessage().getUuid();
            }
            if (conversation.getReceivedMessagesCountSinceUuid(lastMessageUuid) > 0) {
                binding.unreadCountCustomView.setVisibility(View.VISIBLE);
            }
        }
    }
    private int getIndexOf(String uuid, List<Message> messages) {
        if (uuid == null) {
            return messages.size() - 1;
        }
        for (int i = 0; i < messages.size(); ++i) {
            if (uuid.equals(messages.get(i).getUuid())) {
                return i;
            } else {
                Message next = messages.get(i);
                while (next != null && next.wasMergedIntoPrevious()) {
                    if (uuid.equals(next.getUuid())) {
                        return i;
                    }
                    next = next.next();
                }
            }
        }
        return -1;
    }
    private ScrollState getScrollPosition() {
        final ListView listView = this.binding.messagesView;
        if (listView.getCount() == 0 || listView.getLastVisiblePosition() == listView.getCount() - 1) {
            return null;
        } else {
            final int pos = listView.getFirstVisiblePosition();
            final View view = listView.getChildAt(0);
            if (view == null) {
                return null;
            } else {
                return new ScrollState(pos, view.getTop());
            }
        }
    }
    private void setScrollPosition(ScrollState scrollPosition, String lastMessageUuid) {
        if (scrollPosition != null) {
            this.lastMessageUuid = lastMessageUuid;
            if (lastMessageUuid != null) {
                binding.unreadCountCustomView.setUnreadCount(conversation.getReceivedMessagesCountSinceUuid(lastMessageUuid));
            }
            this.binding.messagesView.setSelectionFromTop(scrollPosition.position, scrollPosition.offset);
            toggleScrollDownButton();
        }
    }
    private void attachLocationToConversation(Conversation conversation, Uri uri) {
        if (conversation == null) {
            return;
        }
        activity.xmppConnectionService.attachLocationToConversation(conversation, uri, new UiCallback<Message>() {
            @Override
            public void success(Message message) {
            }
            @Override
            public void error(int errorCode, Message object) {
            }
            @Override
            public void userInputRequried(PendingIntent pi, Message object) {
            }
        });
    }
    private void attachFileToConversation(Conversation conversation, Uri uri, String type) {
        if (conversation == null) {
            return;
        }
        final Toast prepareFileToast = Toast.makeText(getActivity(), getText(R.string.preparing_file), Toast.LENGTH_LONG);
        prepareFileToast.show();
        activity.delegateUriPermissionsToService(uri);
        activity.xmppConnectionService.attachFileToConversation(conversation, uri, type, new UiInformableCallback<Message>() {
            @Override
            public void inform(final String text) {
                hidePrepareFileToast(prepareFileToast);
                runOnUiThread(() -> activity.replaceToast(text));
            }
            @Override
            public void success(Message message) {
                runOnUiThread(() -> activity.hideToast());
                hidePrepareFileToast(prepareFileToast);
            }
            @Override
            public void error(final int errorCode, Message message) {
                hidePrepareFileToast(prepareFileToast);
                runOnUiThread(() -> activity.replaceToast(getString(errorCode)));
            }
            @Override
            public void userInputRequried(PendingIntent pi, Message message) {
                hidePrepareFileToast(prepareFileToast);
            }
        });
    }
    public void attachEditorContentToConversation(Uri uri) {
        mediaPreviewAdapter.addMediaPreviews(Attachment.of(getActivity(), uri, Attachment.Type.FILE));
        toggleInputMethod();
    }
    private void attachImageToConversation(Conversation conversation, Uri uri) {
        if (conversation == null) {
            return;
        }
        final Toast prepareFileToast = Toast.makeText(getActivity(), getText(R.string.preparing_image), Toast.LENGTH_LONG);
        prepareFileToast.show();
        activity.delegateUriPermissionsToService(uri);
        activity.xmppConnectionService.attachImageToConversation(conversation, uri,
                new UiCallback<Message>() {
                    @Override
                    public void userInputRequried(PendingIntent pi, Message object) {
                        hidePrepareFileToast(prepareFileToast);
                    }
                    @Override
                    public void success(Message message) {
                        hidePrepareFileToast(prepareFileToast);
                    }
                    @Override
                    public void error(final int error, Message message) {
                        hidePrepareFileToast(prepareFileToast);
                        activity.runOnUiThread(() -> activity.replaceToast(getString(error)));
                    }
                });
    }
    private void hidePrepareFileToast(final Toast prepareFileToast) {
        if (prepareFileToast != null && activity != null) {
            activity.runOnUiThread(prepareFileToast::cancel);
        }
    }
    private void sendMessage() {
        if (mediaPreviewAdapter.hasAttachments()) {
            commitAttachments();
            return;
        }
        final Editable text = this.binding.textinput.getText();
        final String body =  text == null ? "" : text.toString();
        final Conversation conversation = this.conversation;
        if (body.length() == 0 || conversation == null) {
            return;
        }
        if (conversation.getNextEncryption() == Message.ENCRYPTION_AXOLOTL && trustKeysIfNeeded(REQUEST_TRUST_KEYS_TEXT)) {
            return;
        }
        final Message message;
        if (conversation.getCorrectingMessage() == null) {
            message = new Message(conversation, body, conversation.getNextEncryption());
            if (conversation.getMode() == Conversation.MODE_MULTI) {
                final Jid nextCounterpart = conversation.getNextCounterpart();
                if (nextCounterpart != null) {
                    message.setCounterpart(nextCounterpart);
                    message.setTrueCounterpart(conversation.getMucOptions().getTrueCounterpart(nextCounterpart));
                    message.setType(Message.TYPE_PRIVATE);
                }
            }
        } else {
            message = conversation.getCorrectingMessage();
            message.setBody(body);
            message.setEdited(message.getUuid());
            message.setUuid(UUID.randomUUID().toString());
        }
        switch (conversation.getNextEncryption()) {
            case Message.ENCRYPTION_PGP:
                sendPgpMessage(message);
                break;
            default:
                sendMessage(message);
        }
    }
    protected boolean trustKeysIfNeeded(int requestCode) {
        AxolotlService axolotlService = conversation.getAccount().getAxolotlService();
        final List<Jid> targets = axolotlService.getCryptoTargets(conversation);
        boolean hasUnaccepted = !conversation.getAcceptedCryptoTargets().containsAll(targets);
        boolean hasUndecidedOwn = !axolotlService.getKeysWithTrust(FingerprintStatus.createActiveUndecided()).isEmpty();
        boolean hasUndecidedContacts = !axolotlService.getKeysWithTrust(FingerprintStatus.createActiveUndecided(), targets).isEmpty();
        boolean hasPendingKeys = !axolotlService.findDevicesWithoutSession(conversation).isEmpty();
        boolean hasNoTrustedKeys = axolotlService.anyTargetHasNoTrustedKeys(targets);
        boolean downloadInProgress = axolotlService.hasPendingKeyFetches(targets);
        if (hasUndecidedOwn || hasUndecidedContacts || hasPendingKeys || hasNoTrustedKeys || hasUnaccepted || downloadInProgress) {
            axolotlService.createSessionsIfNeeded(conversation);
            Intent intent = new Intent(getActivity(), TrustKeysActivity.class);
            String[] contacts = new String[targets.size()];
            for (int i = 0; i < contacts.length; ++i) {
                contacts[i] = targets.get(i).toString();
            }
            intent.putExtra("contacts", contacts);
            intent.putExtra(EXTRA_ACCOUNT, conversation.getAccount().getJid().asBareJid().toString());
            intent.putExtra("conversation", conversation.getUuid());
            startActivityForResult(intent, requestCode);
            return true;
        } else {
            return false;
        }
    }
    public void updateChatMsgHint() {
        final boolean multi = conversation.getMode() == Conversation.MODE_MULTI;
        if (conversation.getCorrectingMessage() != null) {
            this.binding.textinput.setHint(R.string.send_corrected_message);
        } else if (multi && conversation.getNextCounterpart() != null) {
            this.binding.textinput.setHint(getString(
                    R.string.send_private_message_to,
                    conversation.getNextCounterpart().getResource()));
        } else if (multi && !conversation.getMucOptions().participating()) {
            this.binding.textinput.setHint(R.string.you_are_not_participating);
        } else {
            this.binding.textinput.setHint(UIHelper.getMessageHint(getActivity(), conversation));
            getActivity().invalidateOptionsMenu();
        }
    }
    public void setupIme() {
        this.binding.textinput.refreshIme();
    }
    private void handleActivityResult(ActivityResult activityResult) {
        if (activityResult.resultCode == Activity.RESULT_OK) {
            handlePositiveActivityResult(activityResult.requestCode, activityResult.data);
        } else {
            handleNegativeActivityResult(activityResult.requestCode);
        }
    }
    private void handlePositiveActivityResult(int requestCode, final Intent data) {
        switch (requestCode) {
            case REQUEST_TRUST_KEYS_TEXT:
                sendMessage();
                break;
            case REQUEST_TRUST_KEYS_ATTACHMENTS:
                commitAttachments();
                break;
            case ATTACHMENT_CHOICE_CHOOSE_IMAGE:
                final List<Attachment> imageUris = Attachment.extractAttachments(getActivity(), data, Attachment.Type.IMAGE);
                mediaPreviewAdapter.addMediaPreviews(imageUris);
                toggleInputMethod();
                break;
            case ATTACHMENT_CHOICE_TAKE_PHOTO:
                final Uri takePhotoUri = pendingTakePhotoUri.pop();
                if (takePhotoUri != null) {
                    mediaPreviewAdapter.addMediaPreviews(Attachment.of(getActivity(), takePhotoUri, Attachment.Type.IMAGE));
                    toggleInputMethod();
                } else {
                    Log.d(Config.LOGTAG, "lost take photo uri. unable to to attach");
                }
                break;
            case ATTACHMENT_CHOICE_CHOOSE_FILE:
            case ATTACHMENT_CHOICE_RECORD_VIDEO:
            case ATTACHMENT_CHOICE_RECORD_VOICE:
                final Attachment.Type type = requestCode == ATTACHMENT_CHOICE_RECORD_VOICE ? Attachment.Type.RECORDING : Attachment.Type.FILE;
                final List<Attachment> fileUris = Attachment.extractAttachments(getActivity(), data, type);
                mediaPreviewAdapter.addMediaPreviews(fileUris);
                toggleInputMethod();
                break;
            case ATTACHMENT_CHOICE_LOCATION:
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                Uri geo = Uri.parse("geo:" + String.valueOf(latitude) + "," + String.valueOf(longitude));
                mediaPreviewAdapter.addMediaPreviews(Attachment.of(getActivity(), geo, Attachment.Type.LOCATION));
                toggleInputMethod();
                break;
            case REQUEST_INVITE_TO_CONVERSATION:
                XmppActivity.ConferenceInvite invite = XmppActivity.ConferenceInvite.parse(data);
                if (invite != null) {
                    if (invite.execute(activity)) {
                        activity.mToast = Toast.makeText(activity, R.string.creating_conference, Toast.LENGTH_LONG);
                        activity.mToast.show();
                    }
                }
                break;
        }
    }
    private void commitAttachments() {
        if (conversation.getNextEncryption() == Message.ENCRYPTION_AXOLOTL && trustKeysIfNeeded(REQUEST_TRUST_KEYS_ATTACHMENTS)) {
            return;
        }
        final List<Attachment> attachments = mediaPreviewAdapter.getAttachments();
        final PresenceSelector.OnPresenceSelected callback = () -> {
            for (Iterator<Attachment> i = attachments.iterator(); i.hasNext(); i.remove()) {
                final Attachment attachment = i.next();
                if (attachment.getType() == Attachment.Type.LOCATION) {
                    attachLocationToConversation(conversation, attachment.getUri());
                } else if (attachment.getType() == Attachment.Type.IMAGE) {
                    Log.d(Config.LOGTAG, "ConversationsActivity.commitAttachments() - attaching image to conversations. CHOOSE_IMAGE");
                    attachImageToConversation(conversation, attachment.getUri());
                } else {
                    Log.d(Config.LOGTAG, "ConversationsActivity.commitAttachments() - attaching file to conversations. CHOOSE_FILE/RECORD_VOICE/RECORD_VIDEO");
                    attachFileToConversation(conversation, attachment.getUri(), attachment.getMime());
                }
            }
            mediaPreviewAdapter.notifyDataSetChanged();
            toggleInputMethod();
        };
        if (conversation == null || conversation.getMode() == Conversation.MODE_MULTI || FileBackend.allFilesUnderSize(getActivity(), attachments, getMaxHttpUploadSize(conversation))) {
            callback.onPresenceSelected();
        } else {
            activity.selectPresence(conversation, callback);
        }
    }
    public void toggleInputMethod() {
        boolean hasAttachments = mediaPreviewAdapter.hasAttachments();
        binding.textinput.setVisibility(hasAttachments ? View.GONE : View.VISIBLE);
        binding.mediaPreview.setVisibility(hasAttachments ? View.VISIBLE : View.GONE);
        updateSendButton();
    }
    private void handleNegativeActivityResult(int requestCode) {
        switch (requestCode) {
            case ATTACHMENT_CHOICE_TAKE_PHOTO:
                if (pendingTakePhotoUri.clear()) {
                    Log.d(Config.LOGTAG, "cleared pending photo uri after negative activity result");
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityResult activityResult = ActivityResult.of(requestCode, resultCode, data);
        if (activity != null && activity.xmppConnectionService != null) {
            handleActivityResult(activityResult);
        } else {
            this.postponedActivityResult.push(activityResult);
        }
    }
    public void unblockConversation(final Blockable conversation) {
        activity.xmppConnectionService.sendUnblockRequest(conversation);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(Config.LOGTAG, "ConversationFragment.onAttach()");
        if (activity instanceof ConversationsActivity) {
            this.activity = (ConversationsActivity) activity;
        } else {
            throw new IllegalStateException("Trying to attach fragment to activity that is not the ConversationsActivity");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        this.activity = null; 
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.fragment_conversation, menu);
        final MenuItem menuMucDetails = menu.findItem(R.id.action_muc_details);
        final MenuItem menuContactDetails = menu.findItem(R.id.action_contact_details);
        final MenuItem menuInviteContact = menu.findItem(R.id.action_invite);
        final MenuItem menuMute = menu.findItem(R.id.action_mute);
        final MenuItem menuUnmute = menu.findItem(R.id.action_unmute);
        if (conversation != null) {
            if (conversation.getMode() == Conversation.MODE_MULTI) {
                menuContactDetails.setVisible(false);
                menuInviteContact.setVisible(conversation.getMucOptions().canInvite());
            } else {
                menuContactDetails.setVisible(!this.conversation.withSelf());
                menuMucDetails.setVisible(false);
                final XmppConnectionService service = activity.xmppConnectionService;
                menuInviteContact.setVisible(service != null && service.findConferenceServer(conversation.getAccount()) != null);
            }
            if (conversation.isMuted()) {
                menuMute.setVisible(false);
            } else {
                menuUnmute.setVisible(false);
            }
            ConversationMenuConfigurator.configureAttachmentMenu(conversation, menu);
            ConversationMenuConfigurator.configureEncryptionMenu(conversation, menu);
        }
        super.onCreateOptionsMenu(menu, menuInflater);
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_conversation, container, false);
        binding.getRoot().setOnClickListener(null); 
        binding.textinput.addTextChangedListener(new StylingHelper.MessageEditorStyler(binding.textinput));
        binding.textinput.setOnEditorActionListener(mEditorActionListener);
        binding.textinput.setRichContentListener(new String[]{"image*");
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    break;
                case ATTACHMENT_CHOICE_RECORD_VOICE:
                    intent = new Intent(getActivity(), RecordingActivity.class);
                    break;
                case ATTACHMENT_CHOICE_LOCATION:
                    intent = GeoHelper.getFetchIntent(activity);
                    break;
            }
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                if (chooser) {
                    startActivityForResult(
                            Intent.createChooser(intent, getString(R.string.perform_action_with)),
                            attachmentChoice);
                } else {
                    startActivityForResult(intent, attachmentChoice);
                }
            }
        };
        if (account.httpUploadAvailable() || attachmentChoice == ATTACHMENT_CHOICE_LOCATION) {
            conversation.setNextCounterpart(null);
            callback.onPresenceSelected();
        } else {
            activity.selectPresence(conversation, callback);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        binding.messagesView.post(this::fireReadEvent);
    }
    private void fireReadEvent() {
        if (activity != null && this.conversation != null) {
            String uuid = getLastVisibleMessageUuid();
            if (uuid != null) {
                activity.onConversationRead(this.conversation, uuid);
            }
        }
    }
    private String getLastVisibleMessageUuid() {
        if (binding == null) {
            return null;
        }
        synchronized (this.messageList) {
            int pos = binding.messagesView.getLastVisiblePosition();
            if (pos >= 0) {
                Message message = null;
                for (int i = pos; i >= 0; --i) {
                    try {
                        message = (Message) binding.messagesView.getItemAtPosition(i);
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }
                    if (message.getType() != Message.TYPE_STATUS) {
                        break;
                    }
                }
                if (message != null) {
                    while (message.next() != null && message.next().wasMergedIntoPrevious()) {
                        message = message.next();
                    }
                    return message.getUuid();
                }
            }
        }
        return null;
    }
    private void showErrorMessage(final Message message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.error_message);
        builder.setMessage(message.getErrorMessage());
        builder.setPositiveButton(R.string.confirm, null);
        builder.create().show();
    }
    private void deleteFile(final Message message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton(R.string.cancel, null);
        builder.setTitle(R.string.delete_file_dialog);
        builder.setMessage(R.string.delete_file_dialog_msg);
        builder.setPositiveButton(R.string.confirm, (dialog, which) -> {
            if (activity.xmppConnectionService.getFileBackend().deleteFile(message)) {
                message.setTransferable(new TransferablePlaceholder(Transferable.STATUS_DELETED));
                activity.onConversationsListItemUpdated();
                refresh();
            }
        });
        builder.create().show();
    }
    private void resendMessage(final Message message) {
        if (message.isFileOrImage()) {
            if (!(message.getConversation() instanceof Conversation)) {
                return;
            }
            final Conversation conversation = (Conversation) message.getConversation();
            DownloadableFile file = activity.xmppConnectionService.getFileBackend().getFile(message);
            if (file.exists()) {
                final XmppConnection xmppConnection = conversation.getAccount().getXmppConnection();
                if (!message.hasFileOnRemoteHost()
                        && xmppConnection != null
                        && !xmppConnection.getFeatures().httpUpload(message.getFileParams().size)) {
                    activity.selectPresence(conversation, () -> {
                        message.setCounterpart(conversation.getNextCounterpart());
                        activity.xmppConnectionService.resendFailedMessages(message);
                        new Handler().post(() -> {
                            int size = messageList.size();
                            this.binding.messagesView.setSelection(size - 1);
                        });
                    });
                    return;
                }
            } else {
                Toast.makeText(activity, R.string.file_deleted, Toast.LENGTH_SHORT).show();
                message.setTransferable(new TransferablePlaceholder(Transferable.STATUS_DELETED));
                activity.onConversationsListItemUpdated();
                refresh();
                return;
            }
        }
        activity.xmppConnectionService.resendFailedMessages(message);
        new Handler().post(() -> {
            int size = messageList.size();
            this.binding.messagesView.setSelection(size - 1);
        });
    }
    private void cancelTransmission(Message message) {
        Transferable transferable = message.getTransferable();
        if (transferable != null) {
            transferable.cancel();
        } else if (message.getStatus() != Message.STATUS_RECEIVED) {
            activity.xmppConnectionService.markMessage(message, Message.STATUS_SEND_FAILED, Message.ERROR_MESSAGE_CANCELLED);
        }
    }
    private void retryDecryption(Message message) {
        message.setEncryption(Message.ENCRYPTION_PGP);
        activity.onConversationsListItemUpdated();
        refresh();
        conversation.getAccount().getPgpDecryptionService().decrypt(message, false);
    }
    public void privateMessageWith(final Jid counterpart) {
        if (conversation.setOutgoingChatState(Config.DEFAULT_CHATSTATE)) {
            activity.xmppConnectionService.sendChatState(conversation);
        }
        this.binding.textinput.setText("");
        this.conversation.setNextCounterpart(counterpart);
        updateChatMsgHint();
        updateSendButton();
        updateEditablity();
    }
    private void correctMessage(Message message) {
        while (message.mergeable(message.next())) {
            message = message.next();
        }
        this.conversation.setCorrectingMessage(message);
        final Editable editable = binding.textinput.getText();
        this.conversation.setDraftMessage(editable.toString());
        this.binding.textinput.setText("");
        this.binding.textinput.append(message.getBody());
    }
    private void highlightInConference(String nick) {
        final Editable editable = this.binding.textinput.getText();
        String oldString = editable.toString().trim();
        final int pos = this.binding.textinput.getSelectionStart();
        if (oldString.isEmpty() || pos == 0) {
            editable.insert(0, nick + ": ");
        } else {
            final char before = editable.charAt(pos - 1);
            final char after = editable.length() > pos ? editable.charAt(pos) : '\0';
            if (before == '\n') {
                editable.insert(pos, nick + ": ");
            } else {
                if (pos > 2 && editable.subSequence(pos - 2, pos).toString().equals(": ")) {
                    if (NickValidityChecker.check(conversation, Arrays.asList(editable.subSequence(0, pos - 2).toString().split(", ")))) {
                        editable.insert(pos - 2, ", " + nick);
                        return;
                    }
                }
                editable.insert(pos, (Character.isWhitespace(before) ? "" : " ") + nick + (Character.isWhitespace(after) ? "" : " "));
                if (Character.isWhitespace(after)) {
                    this.binding.textinput.setSelection(this.binding.textinput.getSelectionStart() + 1);
                }
            }
        }
    }
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        final Activity activity = getActivity();
        if (activity instanceof ConversationsActivity) {
            ((ConversationsActivity) activity).clearPendingViewIntent();
        }
        super.startActivityForResult(intent, requestCode);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (conversation != null) {
            outState.putString(STATE_CONVERSATION_UUID, conversation.getUuid());
            outState.putString(STATE_LAST_MESSAGE_UUID, lastMessageUuid);
            final Uri uri = pendingTakePhotoUri.peek();
            if (uri != null) {
                outState.putString(STATE_PHOTO_URI, uri.toString());
            }
            final ScrollState scrollState = getScrollPosition();
            if (scrollState != null) {
                outState.putParcelable(STATE_SCROLL_POSITION, scrollState);
            }
            final ArrayList<Attachment> attachments = mediaPreviewAdapter.getAttachments();
            if (attachments.size() > 0) {
                outState.putParcelableArrayList(STATE_MEDIA_PREVIEWS, attachments);
            }
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        String uuid = savedInstanceState.getString(STATE_CONVERSATION_UUID);
        ArrayList<Attachment> attachments = savedInstanceState.getParcelableArrayList(STATE_MEDIA_PREVIEWS);
        pendingLastMessageUuid.push(savedInstanceState.getString(STATE_LAST_MESSAGE_UUID, null));
        if (uuid != null) {
            QuickLoader.set(uuid);
            this.pendingConversationsUuid.push(uuid);
            if (attachments != null && attachments.size() > 0) {
                this.pendingMediaPreviews.push(attachments);
            }
            String takePhotoUri = savedInstanceState.getString(STATE_PHOTO_URI);
            if (takePhotoUri != null) {
                pendingTakePhotoUri.push(Uri.parse(takePhotoUri));
            }
            pendingScrollState.push(savedInstanceState.getParcelable(STATE_SCROLL_POSITION));
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        if (this.reInitRequiredOnStart && this.conversation != null) {
            final Bundle extras = pendingExtras.pop();
            reInit(this.conversation, extras != null);
            if (extras != null) {
                processExtras(extras);
            }
        } else if (conversation == null && activity != null && activity.xmppConnectionService != null) {
            final String uuid = pendingConversationsUuid.pop();
            Log.d(Config.LOGTAG, "ConversationFragment.onStart() - activity was bound but no conversation loaded. uuid=" + uuid);
            if (uuid != null) {
                findAndReInitByUuidOrArchive(uuid);
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        final Activity activity = getActivity();
        messageListAdapter.unregisterListenerInAudioPlayer();
        if (activity == null || !activity.isChangingConfigurations()) {
            hideSoftKeyboard(activity);
            messageListAdapter.stopAudioPlayer();
        }
        if (this.conversation != null) {
            final String msg = this.binding.textinput.getText().toString();
            storeNextMessage(msg);
            updateChatState(this.conversation, msg);
            this.activity.xmppConnectionService.getNotificationService().setOpenConversation(null);
        }
        this.reInitRequiredOnStart = true;
    }
    private void updateChatState(final Conversation conversation, final String msg) {
        ChatState state = msg.length() == 0 ? Config.DEFAULT_CHATSTATE : ChatState.PAUSED;
        Account.State status = conversation.getAccount().getStatus();
        if (status == Account.State.ONLINE && conversation.setOutgoingChatState(state)) {
            activity.xmppConnectionService.sendChatState(conversation);
        }
    }
    private void saveMessageDraftStopAudioPlayer() {
        final Conversation previousConversation = this.conversation;
        if (this.activity == null || this.binding == null || previousConversation == null) {
            return;
        }
        Log.d(Config.LOGTAG, "ConversationFragment.saveMessageDraftStopAudioPlayer()");
        final String msg = this.binding.textinput.getText().toString();
        storeNextMessage(msg);
        updateChatState(this.conversation, msg);
        messageListAdapter.stopAudioPlayer();
        mediaPreviewAdapter.clearPreviews();
        toggleInputMethod();
    }
    public void reInit(Conversation conversation, Bundle extras) {
        QuickLoader.set(conversation.getUuid());
        this.saveMessageDraftStopAudioPlayer();
        this.clearPending();
        if (this.reInit(conversation, extras != null)) {
            if (extras != null) {
                processExtras(extras);
            }
            this.reInitRequiredOnStart = false;
        } else {
            this.reInitRequiredOnStart = true;
            pendingExtras.push(extras);
        }
        resetUnreadMessagesCount();
    }
    private void reInit(Conversation conversation) {
        reInit(conversation, false);
    }
    private boolean reInit(final Conversation conversation, final boolean hasExtras) {
        if (conversation == null) {
            return false;
        }
        this.conversation = conversation;
        if (this.activity == null || this.binding == null) {
            return false;
        }
        if (!activity.xmppConnectionService.isConversationStillOpen(this.conversation)) {
            activity.onConversationArchived(this.conversation);
            return false;
        }
        stopScrolling();
        Log.d(Config.LOGTAG, "reInit(hasExtras=" + Boolean.toString(hasExtras) + ")");
        if (this.conversation.isRead() && hasExtras) {
            Log.d(Config.LOGTAG, "trimming conversation");
            this.conversation.trim();
        }
        setupIme();
        final boolean scrolledToBottomAndNoPending = this.scrolledToBottom() && pendingScrollState.peek() == null;
        this.binding.textSendButton.setContentDescription(activity.getString(R.string.send_message_to_x, conversation.getName()));
        this.binding.textinput.setKeyboardListener(null);
        this.binding.textinput.setText("");
        final boolean participating = conversation.getMode() == Conversational.MODE_SINGLE || conversation.getMucOptions().participating();
        if (participating) {
            this.binding.textinput.append(this.conversation.getNextMessage());
        }
        this.binding.textinput.setKeyboardListener(this);
        messageListAdapter.updatePreferences();
        refresh(false);
        this.conversation.messagesLoaded.set(true);
        Log.d(Config.LOGTAG, "scrolledToBottomAndNoPending=" + Boolean.toString(scrolledToBottomAndNoPending));
        if (hasExtras || scrolledToBottomAndNoPending) {
            resetUnreadMessagesCount();
            synchronized (this.messageList) {
                Log.d(Config.LOGTAG, "jump to first unread message");
                final Message first = conversation.getFirstUnreadMessage();
                final int bottom = Math.max(0, this.messageList.size() - 1);
                final int pos;
                final boolean jumpToBottom;
                if (first == null) {
                    pos = bottom;
                    jumpToBottom = true;
                } else {
                    int i = getIndexOf(first.getUuid(), this.messageList);
                    pos = i < 0 ? bottom : i;
                    jumpToBottom = false;
                }
                setSelection(pos, jumpToBottom);
            }
        }
        this.binding.messagesView.post(this::fireReadEvent);
        activity.xmppConnectionService.getNotificationService().setOpenConversation(this.conversation);
        return true;
    }
    private void resetUnreadMessagesCount() {
        lastMessageUuid = null;
        hideUnreadMessagesCount();
    }
    private void hideUnreadMessagesCount() {
        if (this.binding == null) {
            return;
        }
        this.binding.scrollToBottomButton.setEnabled(false);
        this.binding.scrollToBottomButton.hide();
        this.binding.unreadCountCustomView.setVisibility(View.GONE);
    }
    private void setSelection(int pos, boolean jumpToBottom) {
        ListViewUtils.setSelection(this.binding.messagesView, pos, jumpToBottom);
        this.binding.messagesView.post(() -> ListViewUtils.setSelection(this.binding.messagesView, pos, jumpToBottom));
        this.binding.messagesView.post(this::fireReadEvent);
    }
    private boolean scrolledToBottom() {
        return this.binding != null && scrolledToBottom(this.binding.messagesView);
    }
    private void processExtras(Bundle extras) {
        final String downloadUuid = extras.getString(ConversationsActivity.EXTRA_DOWNLOAD_UUID);
        final String text = extras.getString(Intent.EXTRA_TEXT);
        final String nick = extras.getString(ConversationsActivity.EXTRA_NICK);
        final boolean asQuote = extras.getBoolean(ConversationsActivity.EXTRA_AS_QUOTE);
        final boolean pm = extras.getBoolean(ConversationsActivity.EXTRA_IS_PRIVATE_MESSAGE, false);
        final List<Uri> uris = extractUris(extras);
        if (uris != null && uris.size() > 0) {
            final List<Uri> cleanedUris = cleanUris(new ArrayList<>(uris));
            mediaPreviewAdapter.addMediaPreviews(Attachment.of(getActivity(), cleanedUris));
            toggleInputMethod();
            return;
        }
        if (nick != null) {
            if (pm) {
                Jid jid = conversation.getJid();
                try {
                    Jid next = Jid.of(jid.getLocal(), jid.getDomain(), nick);
                    privateMessageWith(next);
                } catch (final IllegalArgumentException ignored) {
                }
            } else {
                final MucOptions mucOptions = conversation.getMucOptions();
                if (mucOptions.participating() || conversation.getNextCounterpart() != null) {
                    highlightInConference(nick);
                }
            }
        } else {
            if (text != null && asQuote) {
                quoteText(text);
            } else {
                appendText(text);
            }
        }
        final Message message = downloadUuid == null ? null : conversation.findMessageWithFileAndUuid(downloadUuid);
        if (message != null) {
            startDownloadable(message);
        }
    }
    private List<Uri> extractUris(Bundle extras) {
        final List<Uri> uris = extras.getParcelableArrayList(Intent.EXTRA_STREAM);
        if (uris != null) {
            return uris;
        }
        final Uri uri = extras.getParcelable(Intent.EXTRA_STREAM);
        if (uri != null) {
            return Collections.singletonList(uri);
        } else {
            return null;
        }
    }
    private List<Uri> cleanUris(List<Uri> uris) {
        Iterator<Uri> iterator = uris.iterator();
        while(iterator.hasNext()) {
            final Uri uri = iterator.next();
            if (FileBackend.weOwnFile(getActivity(), uri)) {
                iterator.remove();
                Toast.makeText(getActivity(), R.string.security_violation_not_attaching_file, Toast.LENGTH_SHORT).show();
            }
        }
        return uris;
    }
    private boolean showBlockSubmenu(View view) {
        final Jid jid = conversation.getJid();
        if (jid.getLocal() == null) {
            BlockContactDialog.show(activity, conversation);
        } else {
            PopupMenu popupMenu = new PopupMenu(getActivity(), view);
            popupMenu.inflate(R.menu.block);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Blockable blockable;
                switch (menuItem.getItemId()) {
                    case R.id.block_domain:
                        blockable = conversation.getAccount().getRoster().getContact(Jid.ofDomain(jid.getDomain()));
                        break;
                    default:
                        blockable = conversation;
                }
                BlockContactDialog.show(activity, blockable);
                return true;
            });
            popupMenu.show();
        }
        return true;
    }
    private void updateSnackBar(final Conversation conversation) {
        final Account account = conversation.getAccount();
        final XmppConnection connection = account.getXmppConnection();
        final int mode = conversation.getMode();
        final Contact contact = mode == Conversation.MODE_SINGLE ? conversation.getContact() : null;
        if (conversation.getStatus() == Conversation.STATUS_ARCHIVED) {
            return;
        }
        if (account.getStatus() == Account.State.DISABLED) {
            showSnackbar(R.string.this_account_is_disabled, R.string.enable, this.mEnableAccountListener);
        } else if (conversation.isBlocked()) {
            showSnackbar(R.string.contact_blocked, R.string.unblock, this.mUnblockClickListener);
        } else if (contact != null && !contact.showInRoster() && contact.getOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST)) {
            showSnackbar(R.string.contact_added_you, R.string.add_back, this.mAddBackClickListener, this.mLongPressBlockListener);
        } else if (contact != null && contact.getOption(Contact.Options.PENDING_SUBSCRIPTION_REQUEST)) {
            showSnackbar(R.string.contact_asks_for_presence_subscription, R.string.allow, this.mAllowPresenceSubscription, this.mLongPressBlockListener);
        } else if (mode == Conversation.MODE_MULTI
                && !conversation.getMucOptions().online()
                && account.getStatus() == Account.State.ONLINE) {
            switch (conversation.getMucOptions().getError()) {
                case NICK_IN_USE:
                    showSnackbar(R.string.nick_in_use, R.string.edit, clickToMuc);
                    break;
                case NO_RESPONSE:
                    showSnackbar(R.string.joining_conference, 0, null);
                    break;
                case SERVER_NOT_FOUND:
                    if (conversation.receivedMessagesCount() > 0) {
                        showSnackbar(R.string.remote_server_not_found, R.string.try_again, joinMuc);
                    } else {
                        showSnackbar(R.string.remote_server_not_found, R.string.leave, leaveMuc);
                    }
                    break;
                case REMOTE_SERVER_TIMEOUT:
                    if (conversation.receivedMessagesCount() > 0) {
                        showSnackbar(R.string.remote_server_timeout, R.string.try_again, joinMuc);
                    } else {
                        showSnackbar(R.string.remote_server_timeout, R.string.leave, leaveMuc);
                    }
                    break;
                case PASSWORD_REQUIRED:
                    showSnackbar(R.string.conference_requires_password, R.string.enter_password, enterPassword);
                    break;
                case BANNED:
                    showSnackbar(R.string.conference_banned, R.string.leave, leaveMuc);
                    break;
                case MEMBERS_ONLY:
                    showSnackbar(R.string.conference_members_only, R.string.leave, leaveMuc);
                    break;
                case RESOURCE_CONSTRAINT:
                    showSnackbar(R.string.conference_resource_constraint, R.string.try_again, joinMuc);
                    break;
                case KICKED:
                    showSnackbar(R.string.conference_kicked, R.string.join, joinMuc);
                    break;
                case UNKNOWN:
                    showSnackbar(R.string.conference_unknown_error, R.string.try_again, joinMuc);
                    break;
                case INVALID_NICK:
                    showSnackbar(R.string.invalid_muc_nick, R.string.edit, clickToMuc);
                case SHUTDOWN:
                    showSnackbar(R.string.conference_shutdown, R.string.try_again, joinMuc);
                    break;
                case DESTROYED:
                    showSnackbar(R.string.conference_destroyed, R.string.leave, leaveMuc);
                    break;
                default:
                    hideSnackbar();
                    break;
            }
        } else if (account.hasPendingPgpIntent(conversation)) {
            showSnackbar(R.string.openpgp_messages_found, R.string.decrypt, clickToDecryptListener);
        } else if (connection != null
                && connection.getFeatures().blocking()
                && conversation.countMessages() != 0
                && !conversation.isBlocked()
                && conversation.isWithStranger()) {
            showSnackbar(R.string.received_message_from_stranger, R.string.block, mBlockClickListener);
        } else {
            hideSnackbar();
        }
    }
    @Override
    public void refresh() {
        if (this.binding == null) {
            Log.d(Config.LOGTAG, "ConversationFragment.refresh() skipped updated because view binding was null");
            return;
        }
        if (this.conversation != null && this.activity != null && this.activity.xmppConnectionService != null) {
            if (!activity.xmppConnectionService.isConversationStillOpen(this.conversation)) {
                activity.onConversationArchived(this.conversation);
                return;
            }
        }
        this.refresh(true);
    }
    private void refresh(boolean notifyConversationRead) {
        synchronized (this.messageList) {
            if (this.conversation != null) {
                conversation.populateWithMessages(this.messageList);
                updateSnackBar(conversation);
                updateStatusMessages();
                if (conversation.getReceivedMessagesCountSinceUuid(lastMessageUuid) != 0) {
                    binding.unreadCountCustomView.setVisibility(View.VISIBLE);
                    binding.unreadCountCustomView.setUnreadCount(conversation.getReceivedMessagesCountSinceUuid(lastMessageUuid));
                }
                this.messageListAdapter.notifyDataSetChanged();
                updateChatMsgHint();
                if (notifyConversationRead && activity != null) {
                    binding.messagesView.post(this::fireReadEvent);
                }
                updateSendButton();
                updateEditablity();
                activity.invalidateOptionsMenu();
            }
        }
    }
    protected void messageSent() {
        mSendingPgpMessage.set(false);
        this.binding.textinput.setText("");
        if (conversation.setCorrectingMessage(null)) {
            this.binding.textinput.append(conversation.getDraftMessage());
            conversation.setDraftMessage(null);
        }
        storeNextMessage();
        updateChatMsgHint();
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(activity);
        final boolean prefScrollToBottom = p.getBoolean("scroll_to_bottom", activity.getResources().getBoolean(R.bool.scroll_to_bottom));
        if (prefScrollToBottom || scrolledToBottom()) {
            new Handler().post(() -> {
                int size = messageList.size();
                this.binding.messagesView.setSelection(size - 1);
            });
        }
    }
    private boolean storeNextMessage() {
        return storeNextMessage(this.binding.textinput.getText().toString());
    }
    private boolean storeNextMessage(String msg) {
        final boolean participating = conversation.getMode() == Conversational.MODE_SINGLE || conversation.getMucOptions().participating();
        if (this.conversation.getStatus() != Conversation.STATUS_ARCHIVED && participating && this.conversation.setNextMessage(msg)) {
            this.activity.xmppConnectionService.updateConversation(this.conversation);
            return true;
        }
        return false;
    }
    public void doneSendingPgpMessage() {
        mSendingPgpMessage.set(false);
    }
    public long getMaxHttpUploadSize(Conversation conversation) {
        final XmppConnection connection = conversation.getAccount().getXmppConnection();
        return connection == null ? -1 : connection.getFeatures().getMaxHttpUploadSize();
    }
    private void updateEditablity() {
        boolean canWrite = this.conversation.getMode() == Conversation.MODE_SINGLE || this.conversation.getMucOptions().participating() || this.conversation.getNextCounterpart() != null;
        this.binding.textinput.setFocusable(canWrite);
        this.binding.textinput.setFocusableInTouchMode(canWrite);
        this.binding.textSendButton.setEnabled(canWrite);
        this.binding.textinput.setCursorVisible(canWrite);
        this.binding.textinput.setEnabled(canWrite);
    }
    public void updateSendButton() {
        boolean hasAttachments = mediaPreviewAdapter != null && mediaPreviewAdapter.hasAttachments();
        boolean useSendButtonToIndicateStatus = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("send_button_status", getResources().getBoolean(R.bool.send_button_status));
        final Conversation c = this.conversation;
        final Presence.Status status;
        final String text = this.binding.textinput == null ? "" : this.binding.textinput.getText().toString();
        final SendButtonAction action;
        if (hasAttachments) {
            action = SendButtonAction.TEXT;
        } else {
            action = SendButtonTool.getAction(getActivity(), c, text);
        }
        if (useSendButtonToIndicateStatus && c.getAccount().getStatus() == Account.State.ONLINE) {
            if (activity.xmppConnectionService != null && activity.xmppConnectionService.getMessageArchiveService().isCatchingUp(c)) {
                status = Presence.Status.OFFLINE;
            } else if (c.getMode() == Conversation.MODE_SINGLE) {
                status = c.getContact().getShownStatus();
            } else {
                status = c.getMucOptions().online() ? Presence.Status.ONLINE : Presence.Status.OFFLINE;
            }
        } else {
            status = Presence.Status.OFFLINE;
        }
        this.binding.textSendButton.setTag(action);
        this.binding.textSendButton.setImageResource(SendButtonTool.getSendButtonImageResource(getActivity(), action, status));
    }
    protected void updateDateSeparators() {
        synchronized (this.messageList) {
            DateSeparator.addAll(this.messageList);
        }
    }
    protected void updateStatusMessages() {
        updateDateSeparators();
        synchronized (this.messageList) {
            if (showLoadMoreMessages(conversation)) {
                this.messageList.add(0, Message.createLoadMoreMessage(conversation));
            }
            if (conversation.getMode() == Conversation.MODE_SINGLE) {
                ChatState state = conversation.getIncomingChatState();
                if (state == ChatState.COMPOSING) {
                    this.messageList.add(Message.createStatusMessage(conversation, getString(R.string.contact_is_typing, conversation.getName())));
                } else if (state == ChatState.PAUSED) {
                    this.messageList.add(Message.createStatusMessage(conversation, getString(R.string.contact_has_stopped_typing, conversation.getName())));
                } else {
                    for (int i = this.messageList.size() - 1; i >= 0; --i) {
                        final Message message = this.messageList.get(i);
                        if (message.getType() != Message.TYPE_STATUS) {
                            if (message.getStatus() == Message.STATUS_RECEIVED) {
                                return;
                            } else {
                                if (message.getStatus() == Message.STATUS_SEND_DISPLAYED) {
                                    this.messageList.add(i + 1,
                                            Message.createStatusMessage(conversation, getString(R.string.contact_has_read_up_to_this_point, conversation.getName())));
                                    return;
                                }
                            }
                        }
                    }
                }
            } else {
                final MucOptions mucOptions = conversation.getMucOptions();
                final List<MucOptions.User> allUsers = mucOptions.getUsers();
                final Set<ReadByMarker> addedMarkers = new HashSet<>();
                ChatState state = ChatState.COMPOSING;
                List<MucOptions.User> users = conversation.getMucOptions().getUsersWithChatState(state, 5);
                if (users.size() == 0) {
                    state = ChatState.PAUSED;
                    users = conversation.getMucOptions().getUsersWithChatState(state, 5);
                }
                if (mucOptions.isPrivateAndNonAnonymous()) {
                    for (int i = this.messageList.size() - 1; i >= 0; --i) {
                        final Set<ReadByMarker> markersForMessage = messageList.get(i).getReadByMarkers();
                        final List<MucOptions.User> shownMarkers = new ArrayList<>();
                        for (ReadByMarker marker : markersForMessage) {
                            if (!ReadByMarker.contains(marker, addedMarkers)) {
                                addedMarkers.add(marker); 
                                MucOptions.User user = mucOptions.findUser(marker);
                                if (user != null && !users.contains(user)) {
                                    shownMarkers.add(user);
                                }
                            }
                        }
                        final ReadByMarker markerForSender = ReadByMarker.from(messageList.get(i));
                        final Message statusMessage;
                        final int size = shownMarkers.size();
                        if (size > 1) {
                            final String body;
                            if (size <= 4) {
                                body = getString(R.string.contacts_have_read_up_to_this_point, UIHelper.concatNames(shownMarkers));
                            } else if (ReadByMarker.allUsersRepresented(allUsers, markersForMessage, markerForSender)) {
                                body = getString(R.string.everyone_has_read_up_to_this_point);
                            } else {
                                body = getString(R.string.contacts_and_n_more_have_read_up_to_this_point, UIHelper.concatNames(shownMarkers, 3), size - 3);
                            }
                            statusMessage = Message.createStatusMessage(conversation, body);
                            statusMessage.setCounterparts(shownMarkers);
                        } else if (size == 1) {
                            statusMessage = Message.createStatusMessage(conversation, getString(R.string.contact_has_read_up_to_this_point, UIHelper.getDisplayName(shownMarkers.get(0))));
                            statusMessage.setCounterpart(shownMarkers.get(0).getFullJid());
                            statusMessage.setTrueCounterpart(shownMarkers.get(0).getRealJid());
                        } else {
                            statusMessage = null;
                        }
                        if (statusMessage != null) {
                            this.messageList.add(i + 1, statusMessage);
                        }
                        addedMarkers.add(markerForSender);
                        if (ReadByMarker.allUsersRepresented(allUsers, addedMarkers)) {
                            break;
                        }
                    }
                }
                if (users.size() > 0) {
                    Message statusMessage;
                    if (users.size() == 1) {
                        MucOptions.User user = users.get(0);
                        int id = state == ChatState.COMPOSING ? R.string.contact_is_typing : R.string.contact_has_stopped_typing;
                        statusMessage = Message.createStatusMessage(conversation, getString(id, UIHelper.getDisplayName(user)));
                        statusMessage.setTrueCounterpart(user.getRealJid());
                        statusMessage.setCounterpart(user.getFullJid());
                    } else {
                        int id = state == ChatState.COMPOSING ? R.string.contacts_are_typing : R.string.contacts_have_stopped_typing;
                        statusMessage = Message.createStatusMessage(conversation, getString(id, UIHelper.concatNames(users)));
                        statusMessage.setCounterparts(users);
                    }
                    this.messageList.add(statusMessage);
                }
            }
        }
    }
    private void stopScrolling() {
        long now = SystemClock.uptimeMillis();
        MotionEvent cancel = MotionEvent.obtain(now, now, MotionEvent.ACTION_CANCEL, 0, 0, 0);
        binding.messagesView.dispatchTouchEvent(cancel);
    }
    private boolean showLoadMoreMessages(final Conversation c) {
        if (activity == null || activity.xmppConnectionService == null) {
            return false;
        }
        final boolean mam = hasMamSupport(c) && !c.getContact().isBlocked();
        final MessageArchiveService service = activity.xmppConnectionService.getMessageArchiveService();
        return mam && (c.getLastClearHistory().getTimestamp() != 0 || (c.countMessages() == 0 && c.messagesLoaded.get() && c.hasMessagesLeftOnServer() && !service.queryInProgress(c)));
    }
    private boolean hasMamSupport(final Conversation c) {
        if (c.getMode() == Conversation.MODE_SINGLE) {
            final XmppConnection connection = c.getAccount().getXmppConnection();
            return connection != null && connection.getFeatures().mam();
        } else {
            return c.getMucOptions().mamSupport();
        }
    }
    protected void showSnackbar(final int message, final int action, final OnClickListener clickListener) {
        showSnackbar(message, action, clickListener, null);
    }
    protected void showSnackbar(final int message, final int action, final OnClickListener clickListener, final View.OnLongClickListener longClickListener) {
        this.binding.snackbar.setVisibility(View.VISIBLE);
        this.binding.snackbar.setOnClickListener(null);
        this.binding.snackbarMessage.setText(message);
        this.binding.snackbarMessage.setOnClickListener(null);
        this.binding.snackbarAction.setVisibility(clickListener == null ? View.GONE : View.VISIBLE);
        if (action != 0) {
            this.binding.snackbarAction.setText(action);
        }
        this.binding.snackbarAction.setOnClickListener(clickListener);
        this.binding.snackbarAction.setOnLongClickListener(longClickListener);
    }
    protected void hideSnackbar() {
        this.binding.snackbar.setVisibility(View.GONE);
    }
    protected void sendMessage(Message message) {
        activity.xmppConnectionService.sendMessage(message);
        messageSent();
    }
    protected void sendPgpMessage(final Message message) {
        final XmppConnectionService xmppService = activity.xmppConnectionService;
        final Contact contact = message.getConversation().getContact();
        if (!activity.hasPgp()) {
            activity.showInstallPgpDialog();
            return;
        }
        if (conversation.getAccount().getPgpSignature() == null) {
            activity.announcePgp(conversation.getAccount(), conversation, null, activity.onOpenPGPKeyPublished);
            return;
        }
        if (!mSendingPgpMessage.compareAndSet(false, true)) {
            Log.d(Config.LOGTAG, "sending pgp message already in progress");
        }
        if (conversation.getMode() == Conversation.MODE_SINGLE) {
            if (contact.getPgpKeyId() != 0) {
                xmppService.getPgpEngine().hasKey(contact,
                        new UiCallback<Contact>() {
                            @Override
                            public void userInputRequried(PendingIntent pi, Contact contact) {
                                startPendingIntent(pi, REQUEST_ENCRYPT_MESSAGE);
                            }
                            @Override
                            public void success(Contact contact) {
                                encryptTextMessage(message);
                            }
                            @Override
                            public void error(int error, Contact contact) {
                                activity.runOnUiThread(() -> Toast.makeText(activity,
                                        R.string.unable_to_connect_to_keychain,
                                        Toast.LENGTH_SHORT
                                ).show());
                                mSendingPgpMessage.set(false);
                            }
                        });
            } else {
                showNoPGPKeyDialog(false, (dialog, which) -> {
                    conversation.setNextEncryption(Message.ENCRYPTION_NONE);
                    xmppService.updateConversation(conversation);
                    message.setEncryption(Message.ENCRYPTION_NONE);
                    xmppService.sendMessage(message);
                    messageSent();
                });
            }
        } else {
            if (conversation.getMucOptions().pgpKeysInUse()) {
                if (!conversation.getMucOptions().everybodyHasKeys()) {
                    Toast warning = Toast
                            .makeText(getActivity(),
                                    R.string.missing_public_keys,
                                    Toast.LENGTH_LONG);
                    warning.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    warning.show();
                }
                encryptTextMessage(message);
            } else {
                showNoPGPKeyDialog(true, (dialog, which) -> {
                    conversation.setNextEncryption(Message.ENCRYPTION_NONE);
                    message.setEncryption(Message.ENCRYPTION_NONE);
                    xmppService.updateConversation(conversation);
                    xmppService.sendMessage(message);
                    messageSent();
                });
            }
        }
    }
    public void encryptTextMessage(Message message) {
        activity.xmppConnectionService.getPgpEngine().encrypt(message,
                new UiCallback<Message>() {
                    @Override
                    public void userInputRequried(PendingIntent pi, Message message) {
                        startPendingIntent(pi, REQUEST_SEND_MESSAGE);
                    }
                    @Override
                    public void success(Message message) {
                        getActivity().runOnUiThread(() -> messageSent());
                    }
                    @Override
                    public void error(final int error, Message message) {
                        getActivity().runOnUiThread(() -> {
                            doneSendingPgpMessage();
                            Toast.makeText(getActivity(), R.string.unable_to_connect_to_keychain, Toast.LENGTH_SHORT).show();
                        });
                    }
                });
    }
    public void showNoPGPKeyDialog(boolean plural, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        if (plural) {
            builder.setTitle(getString(R.string.no_pgp_keys));
            builder.setMessage(getText(R.string.contacts_have_no_pgp_keys));
        } else {
            builder.setTitle(getString(R.string.no_pgp_key));
            builder.setMessage(getText(R.string.contact_has_no_pgp_key));
        }
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.send_unencrypted), listener);
        builder.create().show();
    }
    public void appendText(String text) {
        if (text == null) {
            return;
        }
        String previous = this.binding.textinput.getText().toString();
        if (UIHelper.isLastLineQuote(previous)) {
            text = '\n' + text;
        } else if (previous.length() != 0 && !Character.isWhitespace(previous.charAt(previous.length() - 1))) {
            text = " " + text;
        }
        this.binding.textinput.append(text);
    }
    @Override
    public boolean onEnterPressed() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final boolean enterIsSend = p.getBoolean("enter_is_send", getResources().getBoolean(R.bool.enter_is_send));
        if (enterIsSend) {
            sendMessage();
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onTypingStarted() {
        final XmppConnectionService service = activity == null ? null : activity.xmppConnectionService;
        if (service == null) {
            return;
        }
        Account.State status = conversation.getAccount().getStatus();
        if (status == Account.State.ONLINE && conversation.setOutgoingChatState(ChatState.COMPOSING)) {
            service.sendChatState(conversation);
        }
        updateSendButton();
    }
    @Override
    public void onTypingStopped() {
        final XmppConnectionService service = activity == null ? null : activity.xmppConnectionService;
        if (service == null) {
            return;
        }
        Account.State status = conversation.getAccount().getStatus();
        if (status == Account.State.ONLINE && conversation.setOutgoingChatState(ChatState.PAUSED)) {
            service.sendChatState(conversation);
        }
    }
    @Override
    public void onTextDeleted() {
        final XmppConnectionService service = activity == null ? null : activity.xmppConnectionService;
        if (service == null) {
            return;
        }
        Account.State status = conversation.getAccount().getStatus();
        if (status == Account.State.ONLINE && conversation.setOutgoingChatState(Config.DEFAULT_CHATSTATE)) {
            service.sendChatState(conversation);
        }
        if (storeNextMessage()) {
            activity.onConversationsListItemUpdated();
        }
        updateSendButton();
    }
    @Override
    public void onTextChanged() {
        if (conversation != null && conversation.getCorrectingMessage() != null) {
            updateSendButton();
        }
    }
    @Override
    public boolean onTabPressed(boolean repeated) {
        if (conversation == null || conversation.getMode() == Conversation.MODE_SINGLE) {
            return false;
        }
        if (repeated) {
            completionIndex++;
        } else {
            lastCompletionLength = 0;
            completionIndex = 0;
            final String content = this.binding.textinput.getText().toString();
            lastCompletionCursor = this.binding.textinput.getSelectionEnd();
            int start = lastCompletionCursor > 0 ? content.lastIndexOf(" ", lastCompletionCursor - 1) + 1 : 0;
            firstWord = start == 0;
            incomplete = content.substring(start, lastCompletionCursor);
        }
        List<String> completions = new ArrayList<>();
        for (MucOptions.User user : conversation.getMucOptions().getUsers()) {
            String name = user.getName();
            if (name != null && name.startsWith(incomplete)) {
                completions.add(name + (firstWord ? ": " : " "));
            }
        }
        Collections.sort(completions);
        if (completions.size() > completionIndex) {
            String completion = completions.get(completionIndex).substring(incomplete.length());
            this.binding.textinput.getEditableText().delete(lastCompletionCursor, lastCompletionCursor + lastCompletionLength);
            this.binding.textinput.getEditableText().insert(lastCompletionCursor, completion);
            lastCompletionLength = completion.length();
        } else {
            completionIndex = -1;
            this.binding.textinput.getEditableText().delete(lastCompletionCursor, lastCompletionCursor + lastCompletionLength);
            lastCompletionLength = 0;
        }
        return true;
    }
    private void startPendingIntent(PendingIntent pendingIntent, int requestCode) {
        try {
            getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(), requestCode, null, 0, 0, 0);
        } catch (final SendIntentException ignored) {
        }
    }
    @Override
    public void onBackendConnected() {
        Log.d(Config.LOGTAG, "ConversationFragment.onBackendConnected()");
        String uuid = pendingConversationsUuid.pop();
        if (uuid != null) {
            if (!findAndReInitByUuidOrArchive(uuid)) {
                return;
            }
        } else {
            if (!activity.xmppConnectionService.isConversationStillOpen(conversation)) {
                clearPending();
                activity.onConversationArchived(conversation);
                return;
            }
        }
        ActivityResult activityResult = postponedActivityResult.pop();
        if (activityResult != null) {
            handleActivityResult(activityResult);
        }
        clearPending();
    }
    private boolean findAndReInitByUuidOrArchive(@NonNull final String uuid) {
        Conversation conversation = activity.xmppConnectionService.findConversationByUuid(uuid);
        if (conversation == null) {
            clearPending();
            activity.onConversationArchived(null);
            return false;
        }
        reInit(conversation);
        ScrollState scrollState = pendingScrollState.pop();
        String lastMessageUuid = pendingLastMessageUuid.pop();
        List<Attachment> attachments = pendingMediaPreviews.pop();
        if (scrollState != null) {
            setScrollPosition(scrollState, lastMessageUuid);
        }
        if (attachments != null && attachments.size() > 0) {
            Log.d(Config.LOGTAG, "had attachments on restore");
            mediaPreviewAdapter.addMediaPreviews(attachments);
            toggleInputMethod();
        }
        return true;
    }
    private void clearPending() {
        if (postponedActivityResult.clear()) {
            Log.e(Config.LOGTAG, "cleared pending intent with unhandled result left");
        }
        if (pendingScrollState.clear()) {
            Log.e(Config.LOGTAG, "cleared scroll state");
        }
        if (pendingTakePhotoUri.clear()) {
            Log.e(Config.LOGTAG, "cleared pending photo uri");
        }
    }
    public Conversation getConversation() {
        return conversation;
    }
}