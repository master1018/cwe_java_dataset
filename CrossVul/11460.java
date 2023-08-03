
package net.java.sip.communicator.impl.protocol.jabber;
import java.util.*;
import net.java.sip.communicator.impl.protocol.jabber.extensions.carbon.*;
import net.java.sip.communicator.impl.protocol.jabber.extensions.mailnotification.*;
import net.java.sip.communicator.impl.protocol.jabber.extensions.messagecorrection.*;
import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.service.protocol.Message;
import net.java.sip.communicator.service.protocol.event.*;
import net.java.sip.communicator.service.protocol.jabberconstants.*;
import net.java.sip.communicator.util.*;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.*;
import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smack.provider.*;
import org.jivesoftware.smack.util.*;
import org.jivesoftware.smackx.*;
import org.jivesoftware.smackx.packet.*;
public class OperationSetBasicInstantMessagingJabberImpl
    extends AbstractOperationSetBasicInstantMessaging
    implements OperationSetMessageCorrection
{
    private static final Logger logger =
        Logger.getLogger(OperationSetBasicInstantMessagingJabberImpl.class);
    private static final String PNAME_MAX_GMAIL_THREADS_PER_NOTIFICATION
        = "net.java.sip.communicator.impl.protocol.jabber."
            +"MAX_GMAIL_THREADS_PER_NOTIFICATION";
    private Map<String, StoredThreadID> jids
        = new Hashtable<String, StoredThreadID>();
    private Map<String, String> recentJIDForAddress
        = new Hashtable<String, String>();
    private SmackMessageListener smackMessageListener = null;
    public static class StoredThreadID
    {
        long lastUpdatedTime;
        String threadID;
    }
    private static String prefix = StringUtils.randomString(5);
    private static long id = 0;
    private static final long JID_INACTIVITY_TIMEOUT = 10*60*1000;
    private long lastReceivedMailboxResultTime = -1;
    private final ProtocolProviderServiceJabberImpl jabberProvider;
    private OperationSetPersistentPresenceJabberImpl opSetPersPresence = null;
    private static final String OPEN_BODY_TAG = "<body>";
    private static final String CLOSE_BODY_TAG = "</body>";
    private final static String HTML_NAMESPACE =
        "http:
    private List<PacketFilter> packetFilters = new ArrayList<PacketFilter>();
    private boolean isCarbonEnabled = false;
    OperationSetBasicInstantMessagingJabberImpl(
        ProtocolProviderServiceJabberImpl provider)
    {
        this.jabberProvider = provider;
        packetFilters.add(new GroupMessagePacketFilter());
        packetFilters.add(
            new PacketTypeFilter(org.jivesoftware.smack.packet.Message.class));
        provider.addRegistrationStateChangeListener(
                        new RegistrationStateListener());
        ProviderManager man = ProviderManager.getInstance();
        MessageCorrectionExtensionProvider extProvider =
                new MessageCorrectionExtensionProvider();
        man.addExtensionProvider(MessageCorrectionExtension.ELEMENT_NAME,
                MessageCorrectionExtension.NAMESPACE,
                extProvider);
    }
    public Message createMessageWithUID(
        String messageText, String contentType, String messageUID)
    {
        return new MessageJabberImpl(messageText, contentType,
            DEFAULT_MIME_ENCODING, null, messageUID);
    }
    public Message createMessage(String content, String contentType)
    {
        return createMessage(content, contentType, DEFAULT_MIME_ENCODING, null);
    }
    @Override
    public Message createMessage(String content, String contentType,
        String encoding, String subject)
    {
        return new MessageJabberImpl(content, contentType, encoding, subject);
    }
    Message createMessage(String content, String contentType,
            String messageUID)
    {
        return new MessageJabberImpl(content, contentType,
                DEFAULT_MIME_ENCODING, null, messageUID);
    }
    public boolean isOfflineMessagingSupported()
    {
        return true;
    }
    public boolean isContentTypeSupported(String contentType)
    {
        return
            (contentType.equals(DEFAULT_MIME_TYPE)
                || contentType.equals(HTML_MIME_TYPE));
    }
    @Override
    public boolean isContentTypeSupported(String contentType, Contact contact)
    {
        if(contentType.equals(DEFAULT_MIME_TYPE))
            return true;
        else if(contentType.equals(HTML_MIME_TYPE))
        {
            String toJID = recentJIDForAddress.get(contact.getAddress());
            if (toJID == null)
                toJID = contact.getAddress();
            return jabberProvider.isFeatureListSupported(
                        toJID,
                        HTML_NAMESPACE);
        }
        return false;
    }
    private void purgeOldJids()
    {
        long currentTime = System.currentTimeMillis();
        Iterator<Map.Entry<String, StoredThreadID>> entries
            = jids.entrySet().iterator();
        while( entries.hasNext() )
        {
            Map.Entry<String, StoredThreadID> entry = entries.next();
            StoredThreadID target = entry.getValue();
            if (currentTime - target.lastUpdatedTime
                            > JID_INACTIVITY_TIMEOUT)
                entries.remove();
        }
    }
    String getThreadIDForAddress(String jid)
    {
        synchronized(jids)
        {
            purgeOldJids();
            StoredThreadID ta = jids.get(jid);
            if (ta == null)
                return null;
            ta.lastUpdatedTime = System.currentTimeMillis();
            return ta.threadID;
        }
    }
    private void putJidForAddress(String jid, String threadID)
    {
        synchronized(jids)
        {
            purgeOldJids();
            StoredThreadID ta = jids.get(jid);
            if (ta == null)
            {
                ta = new StoredThreadID();
                jids.put(jid, ta);
            }
            recentJIDForAddress.put(StringUtils.parseBareAddress(jid), jid);
            ta.lastUpdatedTime = System.currentTimeMillis();
            ta.threadID = threadID;
        }
    }
    private MessageDeliveredEvent sendMessage(  Contact to,
                                                ContactResource toResource,
                                                Message message,
                                                PacketExtension[] extensions)
    {
        if( !(to instanceof ContactJabberImpl) )
           throw new IllegalArgumentException(
               "The specified contact is not a Jabber contact."
               + to);
        assertConnected();
        org.jivesoftware.smack.packet.Message msg =
            new org.jivesoftware.smack.packet.Message();
        String toJID = null;
        if (toResource != null)
        {
            if(toResource.equals(ContactResource.BASE_RESOURCE))
            {
                toJID = to.getAddress();
            }
            else
                toJID =
                    ((ContactResourceJabberImpl) toResource).getFullJid();
        }
        if (toJID == null)
        {
            toJID = to.getAddress();
        }
        msg.setPacketID(message.getMessageUID());
        msg.setTo(toJID);
        for (PacketExtension ext : extensions)
        {
            msg.addExtension(ext);
        }
        if (logger.isTraceEnabled())
            logger.trace("Will send a message to:" + toJID
                        + " chat.jid=" + toJID);
        MessageDeliveredEvent msgDeliveryPendingEvt
            = new MessageDeliveredEvent(message, to, toResource);
        MessageDeliveredEvent[] transformedEvents = messageDeliveryPendingTransform(msgDeliveryPendingEvt);
        if (transformedEvents == null || transformedEvents.length == 0)
            return null;
        for (MessageDeliveredEvent event : transformedEvents)
        {
            String content = event.getSourceMessage().getContent();
            if (message.getContentType().equals(HTML_MIME_TYPE))
            {
                msg.setBody(Html2Text.extractText(content));
                if (jabberProvider
                    .isFeatureListSupported(toJID, HTML_NAMESPACE))
                {
                    XHTMLManager.addBody(msg, OPEN_BODY_TAG + content
                        + CLOSE_BODY_TAG);
                }
            }
            else
            {
                msg.setBody(content);
            }
            if (event.isMessageEncrypted() && isCarbonEnabled)
            {
                msg.addExtension(new CarbonPacketExtension.PrivateExtension());
            }
            MessageEventManager.addNotificationsRequests(msg, true, false,
                false, true);
            String threadID = getThreadIDForAddress(toJID);
            if (threadID == null)
                threadID = nextThreadID();
            msg.setThread(threadID);
            msg.setType(org.jivesoftware.smack.packet.Message.Type.chat);
            msg.setFrom(jabberProvider.getConnection().getUser());
            jabberProvider.getConnection().sendPacket(msg);
            putJidForAddress(toJID, threadID);
        }
        return new MessageDeliveredEvent(message, to, toResource);
    }
    public void sendInstantMessage(Contact to, Message message)
        throws IllegalStateException, IllegalArgumentException
    {
        sendInstantMessage(to, null, message);
    }
    @Override
    public void sendInstantMessage( Contact to,
                                    ContactResource toResource,
                                    Message message)
        throws  IllegalStateException,
                IllegalArgumentException
    {
        MessageDeliveredEvent msgDelivered =
            sendMessage(to, toResource, message, new PacketExtension[0]);
        fireMessageEvent(msgDelivered);
    }
    public void correctMessage(
        Contact to, ContactResource resource,
        Message message, String correctedMessageUID)
    {
        PacketExtension[] exts = new PacketExtension[1];
        exts[0] = new MessageCorrectionExtension(correctedMessageUID);
        MessageDeliveredEvent msgDelivered
            = sendMessage(to, resource, message, exts);
        msgDelivered.setCorrectedMessageUID(correctedMessageUID);
        fireMessageEvent(msgDelivered);
    }
    private void assertConnected()
        throws IllegalStateException
    {
        if (opSetPersPresence == null)
        {
            throw
                new IllegalStateException(
                        "The provider must be signed on the service before"
                            + " being able to communicate.");
        }
        else
            opSetPersPresence.assertConnected();
    }
    private class RegistrationStateListener
        implements RegistrationStateChangeListener
    {
        public void registrationStateChanged(RegistrationStateChangeEvent evt)
        {
            if (logger.isDebugEnabled())
                logger.debug("The provider changed state from: "
                         + evt.getOldState()
                         + " to: " + evt.getNewState());
            if (evt.getNewState() == RegistrationState.REGISTERING)
            {
                opSetPersPresence
                    = (OperationSetPersistentPresenceJabberImpl)
                        jabberProvider.getOperationSet(
                                OperationSetPersistentPresence.class);
                if(smackMessageListener == null)
                {
                    smackMessageListener = new SmackMessageListener();
                }
                else
                {
                    jabberProvider.getConnection()
                        .removePacketListener(smackMessageListener);
                }
                jabberProvider.getConnection().addPacketListener(
                        smackMessageListener,
                        new AndFilter(
                            packetFilters.toArray(
                                new PacketFilter[packetFilters.size()])));
            }
            else if (evt.getNewState() == RegistrationState.REGISTERED)
            {
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        initAdditionalServices();
                    }
                }).start();
            }
            else if(evt.getNewState() == RegistrationState.UNREGISTERED
                || evt.getNewState() == RegistrationState.CONNECTION_FAILED
                || evt.getNewState() == RegistrationState.AUTHENTICATION_FAILED)
            {
                if(jabberProvider.getConnection() != null)
                {
                    if(smackMessageListener != null)
                        jabberProvider.getConnection().removePacketListener(
                            smackMessageListener);
                }
                smackMessageListener = null;
            }
        }
    }
    private void initAdditionalServices()
    {
        boolean enableGmailNotifications
            = jabberProvider
            .getAccountID()
            .getAccountPropertyBoolean(
                "GMAIL_NOTIFICATIONS_ENABLED",
                false);
        if (enableGmailNotifications)
            subscribeForGmailNotifications();
        boolean enableCarbon
            = isCarbonSupported() && !jabberProvider.getAccountID()
            .getAccountPropertyBoolean(
                ProtocolProviderFactory.IS_CARBON_DISABLED,
                false);
        if(enableCarbon)
        {
            enableDisableCarbon(true);
        }
        else
        {
            isCarbonEnabled = false;
        }
    }
    private void enableDisableCarbon(final boolean enable)
    {
        IQ iq = new IQ(){
            @Override
            public String getChildElementXML()
            {
                return "<" + (enable? "enable" : "disable") + " xmlns='urn:xmpp:carbons:2' />";
            }
        };
        Packet response = null;
        try
        {
            PacketCollector packetCollector
                = jabberProvider.getConnection().createPacketCollector(
                        new PacketIDFilter(iq.getPacketID()));
            iq.setFrom(jabberProvider.getOurJID());
            iq.setType(IQ.Type.SET);
            jabberProvider.getConnection().sendPacket(iq);
            response
                = packetCollector.nextResult(
                        SmackConfiguration.getPacketReplyTimeout());
            packetCollector.cancel();
        }
        catch(Exception e)
        {
            logger.error("Failed to enable carbon.", e);
        }
        isCarbonEnabled = false;
        if (response == null)
        {
            logger.error(
                    "Failed to enable carbon. No response is received.");
        }
        else if (response.getError() != null)
        {
            logger.error(
                    "Failed to enable carbon: "
                        + response.getError());
        }
        else if (!(response instanceof IQ)
            || !((IQ) response).getType().equals(IQ.Type.RESULT))
        {
            logger.error(
                    "Failed to enable carbon. The response is not correct.");
        }
        else
        {
            isCarbonEnabled = true;
        }
    }
    private boolean isCarbonSupported()
    {
        try
        {
            return jabberProvider.getDiscoveryManager().discoverInfo(
                jabberProvider.getAccountID().getService())
                .containsFeature(CarbonPacketExtension.NAMESPACE);
        }
        catch (XMPPException e)
        {
           logger.warn("Failed to retrieve carbon support." + e.getMessage());
        }
        return false;
    }
    @SuppressWarnings("unchecked")
    private class SmackMessageListener
        implements PacketListener
    {
        public void processPacket(Packet packet)
        {
            if(!(packet instanceof org.jivesoftware.smack.packet.Message))
                return;
            org.jivesoftware.smack.packet.Message msg =
                (org.jivesoftware.smack.packet.Message)packet;
            boolean isForwardedSentMessage = false;
            if(msg.getBody() == null)
            {
                CarbonPacketExtension carbonExt
                    = (CarbonPacketExtension) msg.getExtension(
                        CarbonPacketExtension.NAMESPACE);
                if(carbonExt == null)
                    return;
                isForwardedSentMessage
                    = (carbonExt.getElementName()
                        == CarbonPacketExtension.SENT_ELEMENT_NAME);
                List<ForwardedPacketExtension> extensions
                    = carbonExt.getChildExtensionsOfType(
                        ForwardedPacketExtension.class);
                if(extensions.isEmpty())
                    return;
                if (!msg.getFrom().equals(
                        StringUtils.parseBareAddress(
                            jabberProvider.getOurJID())))
                {
                    logger.info("Received a carbon copy with wrong from!");
                    return;
                }
                ForwardedPacketExtension forwardedExt = extensions.get(0);
                msg = forwardedExt.getMessage();
                if(msg == null || msg.getBody() == null)
                    return;
            }
            Object multiChatExtension =
                msg.getExtension("x", "http:
            if(multiChatExtension != null)
                return;
            String userFullId
                = isForwardedSentMessage? msg.getTo() : msg.getFrom();
            String userBareID = StringUtils.parseBareAddress(userFullId);
            boolean isPrivateMessaging = false;
            ChatRoom privateContactRoom = null;
            OperationSetMultiUserChatJabberImpl mucOpSet =
                (OperationSetMultiUserChatJabberImpl)jabberProvider
                    .getOperationSet(OperationSetMultiUserChat.class);
            if(mucOpSet != null)
                privateContactRoom = mucOpSet.getChatRoom(userBareID);
            if(privateContactRoom != null)
            {
                isPrivateMessaging = true;
            }
            if(logger.isDebugEnabled())
            {
                if (logger.isDebugEnabled())
                    logger.debug("Received from "
                             + userBareID
                             + " the message "
                             + msg.toXML());
            }
            Message newMessage = createMessage(msg.getBody(),
                    DEFAULT_MIME_TYPE, msg.getPacketID());
            PacketExtension ext = msg.getExtension(
                            "http:
            if(ext != null)
            {
                XHTMLExtension xhtmlExt
                    = (XHTMLExtension)ext;
                Iterator<String> bodies = xhtmlExt.getBodies();
                StringBuffer messageBuff = new StringBuffer();
                while (bodies.hasNext())
                {
                    String body = bodies.next();
                    messageBuff.append(body);
                }
                if (messageBuff.length() > 0)
                {
                    String receivedMessage =
                        messageBuff.toString()
                        .replaceAll("\\<[bB][oO][dD][yY].*?>","")
                        .replaceAll("\\</[bB][oO][dD][yY].*?>","");
                    receivedMessage =
                            receivedMessage.replaceAll("&apos;", "&#39;");
                    newMessage = createMessage(receivedMessage,
                            HTML_MIME_TYPE, msg.getPacketID());
                }
            }
            PacketExtension correctionExtension =
                    msg.getExtension(MessageCorrectionExtension.NAMESPACE);
            String correctedMessageUID = null;
            if (correctionExtension != null)
            {
                correctedMessageUID = ((MessageCorrectionExtension)
                        correctionExtension).getCorrectedMessageUID();
            }
            Contact sourceContact
                = opSetPersPresence.findContactByID(
                    (isPrivateMessaging? userFullId : userBareID));
            if(msg.getType()
                            == org.jivesoftware.smack.packet.Message.Type.error)
            {
                if(isPrivateMessaging && sourceContact == null)
                {
                    if(privateContactRoom != null)
                    {
                        XMPPError error = packet.getError();
                        int errorResultCode
                            = ChatRoomMessageDeliveryFailedEvent.UNKNOWN_ERROR;
                        if(error != null && error.getCode() == 403)
                        {
                            errorResultCode
                                = ChatRoomMessageDeliveryFailedEvent.FORBIDDEN;
                        }
                        String errorReason = error.getMessage();
                        ChatRoomMessageDeliveryFailedEvent evt =
                            new ChatRoomMessageDeliveryFailedEvent(
                                privateContactRoom,
                                null,
                                errorResultCode,
                                errorReason,
                                new Date(),
                                newMessage);
                        ((ChatRoomJabberImpl)privateContactRoom)
                            .fireMessageEvent(evt);
                    }
                    return;
                }
                if (logger.isInfoEnabled())
                    logger.info("Message error received from " + userBareID);
                int errorResultCode = MessageDeliveryFailedEvent.UNKNOWN_ERROR;
                if (packet.getError() != null)
                {
                    int errorCode = packet.getError().getCode();
                    if(errorCode == 503)
                    {
                        org.jivesoftware.smackx.packet.MessageEvent msgEvent =
                            (org.jivesoftware.smackx.packet.MessageEvent)
                                packet.getExtension("x", "jabber:x:event");
                        if(msgEvent != null && msgEvent.isOffline())
                        {
                            errorResultCode =
                                MessageDeliveryFailedEvent
                                    .OFFLINE_MESSAGES_NOT_SUPPORTED;
                        }
                    }
                }
                if (sourceContact == null)
                {
                    sourceContact = opSetPersPresence.createVolatileContact(
                        userFullId, isPrivateMessaging);
                }
                MessageDeliveryFailedEvent ev
                    = new MessageDeliveryFailedEvent(newMessage,
                                                     sourceContact,
                                                     correctedMessageUID,
                                                     errorResultCode);
                if (ev != null)
                    fireMessageEvent(ev);
                return;
            }
            putJidForAddress(userFullId, msg.getThread());
            if(sourceContact == null)
            {
                if (logger.isDebugEnabled())
                    logger.debug("received a message from an unknown contact: "
                                   + userBareID);
                sourceContact = opSetPersPresence
                    .createVolatileContact(
                        userFullId,
                        isPrivateMessaging);
            }
            Date timestamp = new Date();
            PacketExtension delay = msg.getExtension("x", "jabber:x:delay");
            if(delay != null && delay instanceof DelayInformation)
            {
                timestamp = ((DelayInformation)delay).getStamp();
            }
            delay = msg.getExtension("delay", "urn:xmpp:delay");
            if(delay != null && delay instanceof DelayInfo)
            {
                timestamp = ((DelayInfo)delay).getStamp();
            }
            ContactResource resource = ((ContactJabberImpl) sourceContact)
                    .getResourceFromJid(userFullId);
            EventObject msgEvt = null;
            if(!isForwardedSentMessage)
                msgEvt
                    = new MessageReceivedEvent( newMessage,
                                                sourceContact,
                                                resource,
                                                timestamp,
                                                correctedMessageUID,
                                                isPrivateMessaging,
                                                privateContactRoom);
            else
                msgEvt = new MessageDeliveredEvent(newMessage, sourceContact, timestamp);
            if (msgEvt != null)
                fireMessageEvent(msgEvt);
        }
    }
    private static class GroupMessagePacketFilter implements PacketFilter
    {
        public boolean accept(Packet packet)
        {
            if(!(packet instanceof org.jivesoftware.smack.packet.Message))
                return false;
            org.jivesoftware.smack.packet.Message msg
                = (org.jivesoftware.smack.packet.Message) packet;
            return
                !msg.getType().equals(
                        org.jivesoftware.smack.packet.Message.Type.groupchat);
        }
    }
    private void subscribeForGmailNotifications()
    {
        String accountIDService = jabberProvider.getAccountID().getService();
        boolean notificationsAreSupported
            = jabberProvider.isFeatureSupported(
                    accountIDService,
                    NewMailNotificationIQ.NAMESPACE);
        if (!notificationsAreSupported)
        {
            if (logger.isDebugEnabled())
                logger.debug(accountIDService
                        +" does not seem to provide a Gmail notification "
                        +" service so we won't be trying to subscribe for it");
            return;
        }
        if (logger.isDebugEnabled())
            logger.debug(accountIDService
                        +" seems to provide a Gmail notification "
                        +" service so we will try to subscribe for it");
        ProviderManager providerManager = ProviderManager.getInstance();
        providerManager.addIQProvider(
                MailboxIQ.ELEMENT_NAME,
                MailboxIQ.NAMESPACE,
                new MailboxIQProvider());
        providerManager.addIQProvider(
                NewMailNotificationIQ.ELEMENT_NAME,
                NewMailNotificationIQ.NAMESPACE,
                new NewMailNotificationProvider());
        Connection connection = jabberProvider.getConnection();
        connection.addPacketListener(
                new MailboxIQListener(), new PacketTypeFilter(MailboxIQ.class));
        connection.addPacketListener(
                new NewMailNotificationListener(),
                new PacketTypeFilter(NewMailNotificationIQ.class));
        if(opSetPersPresence.getCurrentStatusMessage().equals(
                JabberStatusEnum.OFFLINE))
           return;
        MailboxQueryIQ mailboxQuery = new MailboxQueryIQ();
        if (logger.isTraceEnabled())
            logger.trace("sending mailNotification for acc: "
                    + jabberProvider.getAccountID().getAccountUniqueID());
        jabberProvider.getConnection().sendPacket(mailboxQuery);
    }
    private String createMailboxDescription(MailboxIQ mailboxIQ)
    {
        int threadCount = mailboxIQ.getThreadCount();
        String resourceHeaderKey = threadCount > 1
            ? "service.gui.NEW_GMAIL_MANY_HEADER"
            : "service.gui.NEW_GMAIL_HEADER";
        String resourceFooterKey = threadCount > 1
            ? "service.gui.NEW_GMAIL_MANY_FOOTER"
            : "service.gui.NEW_GMAIL_FOOTER";
        String newMailHeader = JabberActivator.getResources().getI18NString(
            resourceHeaderKey,
            new String[]
                {
                    jabberProvider.getAccountID()
                                .getService(),     
                    mailboxIQ.getUrl(),            
                    Integer.toString( threadCount )
                });
        StringBuilder message = new StringBuilder(newMailHeader);
        message.append("<table width=100% cellpadding=2 cellspacing=0 ");
        message.append("border=0 bgcolor=#e8eef7>");
        Iterator<MailThreadInfo> threads = mailboxIQ.threads();
        String maxThreadsStr = (String)JabberActivator.getConfigurationService()
            .getProperty(PNAME_MAX_GMAIL_THREADS_PER_NOTIFICATION);
        int maxThreads = 5;
        try
        {
            if(maxThreadsStr != null)
                maxThreads = Integer.parseInt(maxThreadsStr);
        }
        catch (NumberFormatException e)
        {
            if (logger.isDebugEnabled())
                logger.debug("Failed to parse max threads count: "+maxThreads
                            +". Going for default.");
        }
        for (int i = 0; i < maxThreads && threads.hasNext(); i++)
        {
            message.append(threads.next().createHtmlDescription());
        }
        message.append("</table><br/>");
        if(threadCount > maxThreads)
        {
            String messageFooter = JabberActivator.getResources().getI18NString(
                resourceFooterKey,
                new String[]
                {
                    mailboxIQ.getUrl(),            
                    Integer.toString(
                        threadCount - maxThreads )
                });
            message.append(messageFooter);
        }
        return message.toString();
    }
    public String getRecentJIDForAddress(String address)
    {
        return recentJIDForAddress.get(address);
    }
    private class MailboxIQListener
        implements PacketListener
    {
        public void processPacket(Packet packet)
        {
            if(packet != null && !(packet instanceof MailboxIQ))
                return;
            MailboxIQ mailboxIQ = (MailboxIQ) packet;
            if(mailboxIQ.getTotalMatched() < 1)
                return;
            Contact sourceContact = opSetPersPresence
                .findContactByID(jabberProvider.getAccountID().getService());
            if(sourceContact == null)
                sourceContact = opSetPersPresence.createVolatileContact(
                                jabberProvider.getAccountID().getService());
            lastReceivedMailboxResultTime = mailboxIQ.getResultTime();
            String newMail = createMailboxDescription(mailboxIQ);
            Message newMailMessage = new MessageJabberImpl(
                newMail, HTML_MIME_TYPE, DEFAULT_MIME_ENCODING, null);
            MessageReceivedEvent msgReceivedEvt = new MessageReceivedEvent(
                newMailMessage, sourceContact, new Date(),
                MessageReceivedEvent.SYSTEM_MESSAGE_RECEIVED);
            fireMessageEvent(msgReceivedEvt);
        }
    }
    private class NewMailNotificationListener
        implements PacketListener
    {
        public void processPacket(Packet packet)
        {
            if(packet != null &&  !(packet instanceof NewMailNotificationIQ))
                return;
            boolean enableGmailNotifications
                = jabberProvider
                    .getAccountID()
                        .getAccountPropertyBoolean(
                            "GMAIL_NOTIFICATIONS_ENABLED",
                            false);
            if (!enableGmailNotifications)
                return;
            if(opSetPersPresence.getCurrentStatusMessage()
                    .equals(JabberStatusEnum.OFFLINE))
                return;
            MailboxQueryIQ mailboxQueryIQ = new MailboxQueryIQ();
            if(lastReceivedMailboxResultTime != -1)
                mailboxQueryIQ.setNewerThanTime(
                                lastReceivedMailboxResultTime);
            if (logger.isTraceEnabled())
                logger.trace(
                "send mailNotification for acc: "
                + jabberProvider.getAccountID().getAccountUniqueID());
            jabberProvider.getConnection().sendPacket(mailboxQueryIQ);
        }
    }
    public long getInactivityTimeout()
    {
        return JID_INACTIVITY_TIMEOUT;
    }
    public void addMessageFilters(PacketFilter filter)
    {
        this.packetFilters.add(filter);
    }
    public static synchronized String nextThreadID() {
        return prefix + Long.toString(id++);
    }
}
