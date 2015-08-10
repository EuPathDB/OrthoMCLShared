package org.orthomcl.shared.model.layout;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ForceNode implements Node {

  private final int id;
  private final Node node;
  private final Map<Integer, ForceEdge> neighbours;

  public ForceNode(final Node node, final int id) throws GraphicsException {
    if (node == null)
      throw new GraphicsException("node cannot be null");
    this.node = node;
    this.id = id;
    this.neighbours = new HashMap<>();
  }

  /**
   * @return the point
   */
  @Override
  public Vector getPoint() {
    return node.getPoint();
  }

  /**
   * @return the id
   */
  public int getId() {
    return id;
  }

  /**
   * @return the node
   */
  public Node getNode() {
    return node;
  }

  public void addNeighbour(int nodeId, ForceEdge edge) {
    neighbours.put(nodeId, edge);
  }

  public Collection<ForceEdge> getNeighbours() {
    return neighbours.values();
  }

  public int getNeighbourCount() {
    return neighbours.size();
  }

  public ForceEdge getEdge(int nodeId) {
    return neighbours.get(nodeId);
  }

  @Override
  public String toString() {
    return id + " point=" + node.getPoint();
  }
}
