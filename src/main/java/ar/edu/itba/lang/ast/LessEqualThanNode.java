package ar.edu.itba.lang.ast;

import ar.edu.itba.lang.compiler.NodeVisitor;

public class LessEqualThanNode extends BinaryOperationNode {



        public LessEqualThanNode(Node first, Node second) {
            super(first, second);
        }

        @Override
        public <T> T accept(NodeVisitor<T> visitor) {
            return visitor.visitLessEqualThanNode(this);
        }

        @Override
        public NodeType getNodeType() {
            return NodeType.LESSEQUALSTHANNODE;
        }


}