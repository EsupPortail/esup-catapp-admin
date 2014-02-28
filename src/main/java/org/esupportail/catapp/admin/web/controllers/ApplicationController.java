package org.esupportail.catapp.admin.web.controllers;

import fj.*;
import fj.data.Array;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import org.esupportail.catapp.admin.domain.beans.ApplicationDTO;
import org.esupportail.catapp.admin.domain.beans.DomaineDTO;
import org.esupportail.catapp.admin.domain.exceptions.CrudException;
import org.esupportail.catapp.admin.domain.services.IApplicationService;
import org.esupportail.catapp.admin.domain.services.IDomaineService;
import org.esupportail.catapp.admin.web.pojo.ApplicationPojo;
import org.esupportail.catapp.admin.web.pojo.DomainePojo;
import org.esupportail.catapp.admin.web.utils.fj.PatchedTreeZipper;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Value;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.ArrayList;
import java.util.List;

import static fj.P.p;
import static fj.data.Array.*;
import static fj.data.Option.fromNull;
import static fj.data.Option.fromString;
import static fj.data.Stream.iterableStream;
import static java.text.Normalizer.Form.NFD;
import static java.text.Normalizer.normalize;
import static java.util.Arrays.asList;
import static javax.faces.application.FacesMessage.SEVERITY_ERROR;
import static javax.faces.application.FacesMessage.SEVERITY_INFO;
import static org.esupportail.catapp.admin.web.utils.Transform.applicationToDTO;
import static org.esupportail.catapp.admin.web.utils.Transform.applicationToPojo;

public class ApplicationController extends AbstractController<ApplicationDTO, ApplicationPojo> {

    private TreeNode[] selectedNodes;

    private IDomaineService domaineService;

    @Value("${group.help.url}")
    private String groupHelpUrl;

    public static final String APPS_URI = "/stylesheets/applications/applications.xhtml?faces-redirect=true";

    private ApplicationController(final IApplicationService applicationService, final IDomaineService domaineService) {
        super(applicationService);
        this.domaineService = domaineService;
        this.pojo = new ApplicationPojo();;
        this.originalPojo = new ApplicationPojo();;
        this.treeNode = buildTreeNode(domaineService);
        iterableArray(this.treeNode.getChildren()).foreach(new Effect<TreeNode>() {
            public void e(TreeNode treeNode) {
                treeNode.setSelectable(false);
            }
        });
    }

    public static ApplicationController applicationController(final IApplicationService applicationService,
                                                              final IDomaineService domaineService) {
        return new ApplicationController(applicationService, domaineService);
    }

    @Override
    public String create() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!isUnique()) {
            facesContext.addMessage("catappform:codeApp", uniqueError);
            return null;
        }

        try {
            service.add(applicationToDTO.f(pojo));
            final String summary = resourceBundle.getString("MESSAGE.SUCCESS.ADD.APPLICATION.TITLE");
            final String detail = resourceBundle.getString("MESSAGE.SUCCESS.ADD.APPLICATION.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);
            facesContext.addMessage(null, facesMessage);

            return APPS_URI;
        } catch (InterruptedException e) {
            facesContext.addMessage(null, serviceUnavailableMsg);
        } catch (CrudException e) {
            final String summary = resourceBundle.getString("ERROR.SERVICE.UNABLE.ADD.APPLICATION.TITLE");
            final String detail = resourceBundle.getString("ERROR.SERVICE.UNABLE.ADD.APPLICATION.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(null, facesMessage);
        }
        return null;
    }

    @Override
    public String update() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            if (!originalPojo.getCode().equalsIgnoreCase(pojo.getCode())) {
                // le code de l'application a été modifié
                if (!isUnique()) {
                    // le code indiqué existe déjà
                    facesContext.addMessage("catappform:codeApp", uniqueError);
                    return null;
                } else {
                    // on créé une nouvelle application avec le nouveau code
                    service.add(applicationToDTO.f(pojo));
                    // et on supprime l'application correspondant à l'ancien code
                    service.delete(originalPojo.getCode());
                }
            } else {
                service.update(applicationToDTO.f(pojo));
            }

            final String summary = resourceBundle.getString("MESSAGE.SUCCESS.UPDATE.APPLICATION.TITLE");
            final String detail = resourceBundle.getString("MESSAGE.SUCCESS.UPDATE.APPLICATION.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);
            facesContext.addMessage(null, facesMessage);

            return APPS_URI;
        } catch (InterruptedException e) {
            facesContext.addMessage(null, serviceUnavailableMsg);
        } catch (CrudException e) {
            final String summary = resourceBundle.getString("ERROR.SERVICE.UNABLE.UPDATE.APPLICATION.TITLE");
            final String detail = resourceBundle.getString("ERROR.SERVICE.UNABLE.UPDATE.APPLICATION.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(null, facesMessage);
        }
        return null;
    }

    @Override
    public String delete() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            service.delete(pojo.getCode());
            final String summary = resourceBundle.getString("MESSAGE.SUCCESS.DELETE.APPLICATION.TITLE");
            final String detail = resourceBundle.getString("MESSAGE.SUCCESS.DELETE.APPLICATION.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);
            facesContext.addMessage(null, facesMessage);

            return APPS_URI;
        } catch (InterruptedException e) {
            facesContext.addMessage(null, serviceUnavailableMsg);
        } catch (CrudException e) {
            final String summary = resourceBundle.getString("ERROR.SERVICE.UNABLE.DELETE.APPLICATION.TITLE");
            final String detail = resourceBundle.getString("ERROR.SERVICE.UNABLE.DELETE.APPLICATION.DESCRIPTION");

            final FacesMessage facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
            facesContext.addMessage(null, facesMessage);
        }
        return null;
    }

    public List<ApplicationPojo> filter(final String query) {
        final F<String, String> sansAccents = new F<String, String>() {
            public String f(String s) {
                return normalize(s, NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            }
        };

        return new ArrayList<>(iterableArray(getApplications()).filter(new F<ApplicationPojo, Boolean>() {
            public Boolean f(ApplicationPojo app) {
                final String check = sansAccents.f(app.getItemLabel().toLowerCase());
                final String req = sansAccents.f(query.toLowerCase());
                return check.contains(req);
            }
        }).toCollection());
    }

    public void onSelectApplication() {
        setPojo(originalPojo);
    }

    public void onSelectNode(final NodeSelectEvent event) {
        setDomainsForSelectedNodes();
    }

    public void onUnSelectNode(final NodeUnselectEvent event) {
        setDomainsForSelectedNodes();
    }

    public void setNodesSelected() {
        final Tree<TreeNode> baseTree = Tree.unfoldTree(new F<TreeNode, P2<TreeNode, P1<Stream<TreeNode>>>>() {
            public P2<TreeNode, P1<Stream<TreeNode>>> f(final TreeNode tn) {
                final P1<Stream<TreeNode>> children = new P1<Stream<TreeNode>>() {
                    public Stream<TreeNode> _1() {
                        return iterableStream(tn.getChildren());
                    }
                };
                return p(tn, children);
            }
        }).f(treeNode);

        Array<TreeNode> nodes = empty();
        for (PatchedTreeZipper<TreeNode> zipper : PatchedTreeZipper.fromTree(baseTree)) {
            for (final String domCode : pojo.getDomaines()) {
                final Option<PatchedTreeZipper<TreeNode>> child = zipper.findChild(new F<Tree<TreeNode>, Boolean>() {
                    public Boolean f(Tree<TreeNode> subTree) {
                        final DomaineDTO dto = (DomaineDTO) subTree.root().getData();
                        return dto.getCode().equalsIgnoreCase(domCode);
                    }
                });
                if (child.isSome()) {
                    final PatchedTreeZipper<TreeNode> childZip = child.some();
                    Option<PatchedTreeZipper<TreeNode>> parent = childZip.parent();
                    while (parent.isSome()) {
                        parent.some().focus().root().setExpanded(true);
                        parent = parent.some().parent();
                    }
                    final TreeNode node = childZip.focus().root();
                    node.setSelected(true);
                    nodes = nodes.append(single(node));
                    break;
                }
            }
        }
        selectedNodes = nodes.array(TreeNode[].class);
        RequestContext.getCurrentInstance().update("catappform:domainesModalContent");
    }

    protected boolean isUnique() {
        return iterableArray(getApplications()).find(new F<ApplicationPojo, Boolean>() {
            public Boolean f(final ApplicationPojo appPojo) {
                return appPojo.getCode().equalsIgnoreCase(pojo.getCode());
            }
        }).isNone();
    }

    private void setDomainsForSelectedNodes() {
        this.pojo.setDomaines(new ArrayList<>(asList(fromNull(selectedNodes).map(new F<TreeNode[], String[]>() {
            public String[] f(TreeNode[] treeNodes) {
                return array(treeNodes).map(new F<TreeNode, String>() {
                    public String f(TreeNode treeNode) {
                        DomaineDTO dto = (DomaineDTO) treeNode.getData();
                        return dto.getCode();
                    }
                }).array(String[].class);
            }
        }).orSome(new String[0]))));
    }

    ///////////////// JSF Convenience

    public Converter getApplicationConverter() {
        return new Converter() {
            public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
                return fromString(value).map(new F<String, ApplicationPojo>() {
                    public ApplicationPojo f(final String code) {
                        final Option<ApplicationPojo> found = iterableArray(getApplications()).find(new F<ApplicationPojo, Boolean>() {
                            public Boolean f(final ApplicationPojo app) {
                                return app.getCode().equalsIgnoreCase(code);
                            }
                        });
                        return found.orSome(new ApplicationPojo());
                    }
                }).orSome(new ApplicationPojo());
            }

            public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
                return fromNull(value).map(new F<Object, String>() {
                    public String f(Object o) {
                        if (o instanceof  ApplicationPojo) {
                            ApplicationPojo app = (ApplicationPojo) o;
                            return app.getCode();
                        }
                        return "";
                    }
                }).orSome("");
            }
        };
    }



    ///////////////// JSF Accessors

    public List<ApplicationPojo> getApplications() {
        try {
            return new ArrayList<>(service.getList().map(applicationToPojo).toCollection());
        } catch (InterruptedException e) {
            FacesContext.getCurrentInstance().addMessage(null, serviceUnavailableMsg);
            return new ArrayList<>();
        }
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(final TreeNode... selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public String getGroupHelpUrl() {
        return groupHelpUrl;
    }

    public List<DomainePojo> getSelectedDomaines() {
        return fromNull(pojo.getDomaines()).map(new F<List<String>, List<DomainePojo>>() {
            public List<DomainePojo> f(List<String> codes) {
                return new ArrayList<>(iterableArray(codes).map(new F<String, DomainePojo>() {
                    public DomainePojo f(String code) {
                        try {
                            final DomaineDTO dto = domaineService.getOne(code);
                            return new DomainePojo()
                                    .withCode(dto.getCode())
                                    .withLibelle(dto.getCaption())
                                    .withParent(dto.getParent())
                                    .withApplications(dto.getApplications());
                        } catch (InterruptedException e) {
                            return new DomainePojo();
                        }
                    }
                }).toCollection());
            }
        }).orSome(new ArrayList<DomainePojo>());
    }
}
