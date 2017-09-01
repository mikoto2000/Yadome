package jp.dip.oyasirazu.yadome;

import org.w3c.dom.Node;

/**
 * NodeVisitor
 */
public interface NodeVisitor {
    public NodeVisitResult visitNode(Node node);
}

