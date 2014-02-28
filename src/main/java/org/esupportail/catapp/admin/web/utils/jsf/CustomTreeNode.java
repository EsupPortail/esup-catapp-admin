package org.esupportail.catapp.admin.web.utils.jsf;


import fj.F;
import fj.Show;
import fj.data.Option;
import fj.data.Stream;
import fj.data.Tree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import java.util.ArrayList;
import java.util.List;

public class CustomTreeNode<T> extends DefaultTreeNode {

    private final Tree<T> self;

    private final Option<TreeNode> parent;

    private final Stream<TreeNode> children;

    public CustomTreeNode(Tree<T> root, Option<TreeNode> parent) {
        self = root;
        this.parent = parent;
        children = self.subForest()._1().map(new F<Tree<T>, TreeNode>() {
            public TreeNode f(Tree<T> subtree) {
                return new CustomTreeNode<>(subtree, Option.<TreeNode>some(CustomTreeNode.this));
            }
        });
        setExpanded(true);
    }

    @Override
    public Object getData() {
        return self.root();
    }

    @Override
    public List<TreeNode> getChildren() {
        return new ArrayList<>(children.toCollection());
    }

    @Override
    public TreeNode getParent() {
        return parent.toNull();
    }

    @Override
    public int getChildCount() {
        return children.length();
    }

    @Override
    public boolean isLeaf() {
        return getChildCount() == 0;
    }

    @Override
    public String toString() {
        return self.draw(Show.<T>anyShow());
    }

    public Tree<T> getSelf() {
        return self;
    }
}
