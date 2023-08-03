
package com.sun.faces.context;
import com.sun.faces.component.visit.PartialVisitContext;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import java.io.Writer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import javax.faces.FactoryFinder;
import static javax.faces.FactoryFinder.VISIT_CONTEXT_FACTORY;
import javax.faces.component.visit.VisitContextFactory;
import javax.faces.component.visit.VisitContextWrapper;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
 public class PartialViewContextImpl extends PartialViewContext {
    private static Logger LOGGER = FacesLogger.CONTEXT.getLogger();
    private boolean released;
    private PartialResponseWriter partialResponseWriter;
    private List<String> executeIds;
    private Collection<String> renderIds;
    private Boolean ajaxRequest;
    private Boolean partialRequest;
    private Boolean renderAll;
    private FacesContext ctx;
    private boolean processingPhases = false;
    private static final String ORIGINAL_WRITER = "com.sun.faces.ORIGINAL_WRITER";
    public PartialViewContextImpl(FacesContext ctx) {
        this.ctx = ctx;
    }
    @Override
    public boolean isAjaxRequest() {
        assertNotReleased();
        if (ajaxRequest == null) {
            ajaxRequest = "partial/ajax".equals(ctx.
                getExternalContext().getRequestHeaderMap().get("Faces-Request"));
            if (!ajaxRequest) {
                ajaxRequest = "partial/ajax".equals(ctx.getExternalContext().getRequestParameterMap().
                    get("Faces-Request"));
            }
        }
        return ajaxRequest;
    }
    @Override
    public boolean isPartialRequest() {
        assertNotReleased();
        if (partialRequest == null) {
            partialRequest = isAjaxRequest() ||
                    "partial/process".equals(ctx.
                    getExternalContext().getRequestHeaderMap().get("Faces-Request"));
        }
        return partialRequest;
    }
    @Override
    public boolean isExecuteAll() {
        assertNotReleased();
        String execute = ctx.
            getExternalContext().getRequestParameterMap()
                .get(PARTIAL_EXECUTE_PARAM_NAME);
        return (ALL_PARTIAL_PHASE_CLIENT_IDS.equals(execute));
    }
    @Override
    public boolean isRenderAll() {
        assertNotReleased();
        if (renderAll == null) {
            String render = ctx.
                getExternalContext().getRequestParameterMap()
                    .get(PARTIAL_RENDER_PARAM_NAME);
            renderAll = (ALL_PARTIAL_PHASE_CLIENT_IDS.equals(render));
        }
        return renderAll;
    }
    @Override
    public void setRenderAll(boolean renderAll) {
        this.renderAll = renderAll;
    }
    @Override
    public boolean isResetValues() {
        Object value = ctx.getExternalContext().getRequestParameterMap().get(RESET_VALUES_PARAM_NAME);
        return (null != value && "true".equals(value)) ? true : false;
    }
    @Override
    public void setPartialRequest(boolean isPartialRequest) {
        this.partialRequest = isPartialRequest;
    }
    @Override
    public Collection<String> getExecuteIds() {
        assertNotReleased();
        if (executeIds != null) {
            return executeIds;
        }
        executeIds = populatePhaseClientIds(PARTIAL_EXECUTE_PARAM_NAME);
        if (!executeIds.isEmpty()) {
            UIViewRoot root = ctx.getViewRoot();
            if (root.getFacetCount() > 0) {
                if (root.getFacet(UIViewRoot.METADATA_FACET_NAME) != null) {
                    executeIds.add(0, UIViewRoot.METADATA_FACET_NAME);   
                }
            }
        }
        return executeIds;
    }
    @Override
    public Collection<String> getRenderIds() {
        assertNotReleased();
        if (renderIds != null) {
            return renderIds;
        }
        renderIds = populatePhaseClientIds(PARTIAL_RENDER_PARAM_NAME);
        return renderIds;
    }
    @Override
    public void processPartial(PhaseId phaseId) {
        PartialViewContext pvc = ctx.getPartialViewContext();
        Collection <String> myExecuteIds = pvc.getExecuteIds();
        Collection <String> myRenderIds = pvc.getRenderIds();
        UIViewRoot viewRoot = ctx.getViewRoot();
        if (phaseId == PhaseId.APPLY_REQUEST_VALUES ||
            phaseId == PhaseId.PROCESS_VALIDATIONS ||
            phaseId == PhaseId.UPDATE_MODEL_VALUES) {
            if (myExecuteIds == null || myExecuteIds.isEmpty()) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                        "No execute and render identifiers specified.  Skipping component processing.");
                }
                return;
            }
            try {
                processComponents(viewRoot, phaseId, myExecuteIds, ctx);
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                           e.toString(),
                           e);
                }
                throw new FacesException(e);
            }
            if (phaseId == PhaseId.APPLY_REQUEST_VALUES) {
                PartialResponseWriter writer = pvc.getPartialResponseWriter();
                ctx.setResponseWriter(writer);
            }
        } else if (phaseId == PhaseId.RENDER_RESPONSE) {
            try {
                PartialResponseWriter writer = pvc.getPartialResponseWriter();
                ResponseWriter orig = ctx.getResponseWriter();
                ctx.getAttributes().put(ORIGINAL_WRITER, orig);
                ctx.setResponseWriter(writer);
                ExternalContext exContext = ctx.getExternalContext();
                exContext.setResponseContentType("text/xml");
                exContext.addResponseHeader("Cache-Control", "no-cache");
                writer.startDocument();
                if (isResetValues()) {
                    viewRoot.resetValues(ctx, myRenderIds);
                }
                if (isRenderAll()) {
                    renderAll(ctx, viewRoot);
                    renderState(ctx);
                    writer.endDocument();
                    return;
                }
                if (myRenderIds != null && !myRenderIds.isEmpty()) {
                    processComponents(viewRoot, phaseId, myRenderIds, ctx);
                }
                renderState(ctx);
                writer.endDocument();
            } catch (IOException ex) {
                this.cleanupAfterView();
            } catch (RuntimeException ex) {
                this.cleanupAfterView();
                throw ex;
            }
        }
    }
    @Override
    public PartialResponseWriter getPartialResponseWriter() {
        assertNotReleased();
        if (partialResponseWriter == null) {
            partialResponseWriter = new DelayedInitPartialResponseWriter(this);
        }
        return partialResponseWriter;
    }
    public void release() {
        released = true;
        ajaxRequest = null;
        renderAll = null;
        partialResponseWriter = null;
        executeIds = null;
        renderIds = null;
        ctx = null;
        partialRequest = null;
    }
    private List<String> populatePhaseClientIds(String parameterName) {
        Map<String,String> requestParamMap =
              ctx.getExternalContext().getRequestParameterMap();
        String param = requestParamMap.get(parameterName);
        if (param == null) {
            return new ArrayList<String>();
        } else {
            Map<String, Object> appMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
            String[] pcs = Util.split(appMap, param, "[ \t]+");
            return ((pcs != null && pcs.length != 0)
                    ? new ArrayList<String>(Arrays.asList(pcs))
                    : new ArrayList<String>());
        }
    }
    private void processComponents(UIComponent component, PhaseId phaseId,
        Collection<String> phaseClientIds, FacesContext context) throws IOException {
        EnumSet<VisitHint> hints = EnumSet.of(VisitHint.SKIP_UNRENDERED, VisitHint.EXECUTE_LIFECYCLE);
        VisitContextFactory visitContextFactory = (VisitContextFactory) 
                FactoryFinder.getFactory(VISIT_CONTEXT_FACTORY);
        VisitContext visitContext = visitContextFactory.getVisitContext(context, phaseClientIds, hints);
        PhaseAwareVisitCallback visitCallback =
            new PhaseAwareVisitCallback(ctx, phaseId);
        component.visitTree(visitContext, visitCallback);
        PartialVisitContext partialVisitContext = unwrapPartialVisitContext(visitContext);
        if (partialVisitContext != null) {
            if (LOGGER.isLoggable(Level.FINER) && !partialVisitContext.getUnvisitedClientIds().isEmpty()) {
                Collection<String> unvisitedClientIds = partialVisitContext.getUnvisitedClientIds();
                String message;
                StringBuilder builder = new StringBuilder();
                for (String cur : unvisitedClientIds) {
                    builder.append(cur).append(" ");
                }
                LOGGER.log(Level.FINER,
                        "jsf.context.partial_visit_context_unvisited_children",
                        new Object[]{builder.toString()});
            }
        }    
    }
    private static PartialVisitContext unwrapPartialVisitContext(VisitContext visitContext) {
        if (visitContext == null) {
            return null;
        }
        if (visitContext instanceof PartialVisitContext) {
            return (PartialVisitContext) visitContext;
        }
        if (visitContext instanceof VisitContextWrapper) {
            return unwrapPartialVisitContext(((VisitContextWrapper) visitContext).getWrapped());
        }
        return null;
    }
    private void renderAll(FacesContext context, UIViewRoot viewRoot) throws IOException {
        PartialViewContext pvc = context.getPartialViewContext();
        PartialResponseWriter writer = pvc.getPartialResponseWriter();
        if (!Util.isPortletRequest(context)) {
            writer.startUpdate(PartialResponseWriter.RENDER_ALL_MARKER);
            if (viewRoot.getChildCount() > 0) {
                for (UIComponent uiComponent : viewRoot.getChildren()) {
                    uiComponent.encodeAll(context);
                }
            }
            writer.endUpdate();
        }
        else {
            writer.startUpdate(viewRoot.getClientId(ctx));
            viewRoot.encodeBegin(context);
            if (viewRoot.getChildCount() > 0) {
                for (UIComponent uiComponent : viewRoot.getChildren()) {
                    uiComponent.encodeAll(context);
                }
            }
            viewRoot.encodeEnd(context);
            writer.endUpdate();
        }
    }
    private void renderState(FacesContext context) throws IOException {
        PartialViewContext pvc = context.getPartialViewContext();
        PartialResponseWriter writer = pvc.getPartialResponseWriter();
        String viewStateId = Util.getViewStateId(context);
        writer.startUpdate(viewStateId);
        String state = context.getApplication().getStateManager().getViewState(context);
        writer.write(state);
        writer.endUpdate();
        ClientWindow window = context.getExternalContext().getClientWindow();
        if (null != window) {
            String clientWindowId = Util.getClientWindowId(context);
            writer.startUpdate(clientWindowId);
            writer.write(window.getId());
            writer.endUpdate();
        }
    }
    private PartialResponseWriter createPartialResponseWriter() {
        ExternalContext extContext = ctx.getExternalContext();
        String encoding = extContext.getRequestCharacterEncoding();
        extContext.setResponseCharacterEncoding(encoding);
        ResponseWriter responseWriter = null;
        Writer out = null;
        try {
            out = extContext.getResponseOutputWriter();
        } catch (IOException ioe) {
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                           ioe.toString(),
                           ioe);
            }
        }
        if (out != null) {
            UIViewRoot viewRoot = ctx.getViewRoot();
            if (viewRoot != null) {
                responseWriter =
                    ctx.getRenderKit().createResponseWriter(out,
                    "text/xml", encoding);
            } else {
                RenderKitFactory factory = (RenderKitFactory)
                    FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
                RenderKit renderKit = factory.getRenderKit(ctx, RenderKitFactory.HTML_BASIC_RENDER_KIT);
                responseWriter = renderKit.createResponseWriter(out, "text/xml", encoding);
            }
        }
        if (responseWriter instanceof PartialResponseWriter)  {
            return (PartialResponseWriter) responseWriter;
        } else {
            return new PartialResponseWriter(responseWriter);
        }
    }
    private void cleanupAfterView() {
        ResponseWriter orig = (ResponseWriter) ctx.getAttributes().
            get(ORIGINAL_WRITER);
        assert(null != orig);
        ctx.setResponseWriter(orig);
    }
    @SuppressWarnings({"FinalPrivateMethod"})
    private final void assertNotReleased() {
        if (released) {
            throw new IllegalStateException();
        }
    }
    private static class PhaseAwareVisitCallback implements VisitCallback {
        private PhaseId curPhase;
        private FacesContext ctx;
        private PhaseAwareVisitCallback(FacesContext ctx, PhaseId curPhase) {
            this.ctx = ctx;
            this.curPhase = curPhase;
        }  
        public VisitResult visit(VisitContext context, UIComponent comp) {
            try {
                if (curPhase == PhaseId.APPLY_REQUEST_VALUES) {
                    comp.processDecodes(ctx);
                } else if (curPhase == PhaseId.PROCESS_VALIDATIONS) {
                    comp.processValidators(ctx);
                } else if (curPhase == PhaseId.UPDATE_MODEL_VALUES) {
                    comp.processUpdates(ctx);
                } else if (curPhase == PhaseId.RENDER_RESPONSE) {
                    PartialResponseWriter writer = ctx.getPartialViewContext().getPartialResponseWriter();
                    writer.startUpdate(comp.getClientId(ctx));
                    comp.encodeAll(ctx);
                    writer.endUpdate();
                } else {
                    throw new IllegalStateException("I18N: Unexpected " +
                                                    "PhaseId passed to " +
                                              " PhaseAwareContextCallback: " +
                                                    curPhase.toString());
                }
            }
            catch (IOException ex) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.severe(ex.toString());
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                    ex.toString(),
                    ex);
                }
                throw new FacesException(ex);
            }
            return VisitResult.REJECT;
        }
    }
    private static final class DelayedInitPartialResponseWriter extends PartialResponseWriter {
        private ResponseWriter writer;
        private PartialViewContextImpl ctx;
        public DelayedInitPartialResponseWriter(PartialViewContextImpl ctx) {
            super(null);
            this.ctx = ctx;
            ExternalContext extCtx = ctx.ctx.getExternalContext();
            extCtx.setResponseContentType("text/xml");
            extCtx.setResponseCharacterEncoding(extCtx.getRequestCharacterEncoding());
        }
        @Override
        public ResponseWriter getWrapped() {
            if (writer == null) {
                writer = ctx.createPartialResponseWriter();
            }
            return writer;
        }
    } 
} 
