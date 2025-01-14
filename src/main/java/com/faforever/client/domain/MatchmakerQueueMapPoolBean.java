package com.faforever.client.domain;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class MatchmakerQueueMapPoolBean extends AbstractEntityBean {
  private final DoubleProperty minRating = new SimpleDoubleProperty();
  private final DoubleProperty maxRating = new SimpleDoubleProperty();
  private final ObjectProperty<MatchmakerQueueBean> matchmakerQueue = new SimpleObjectProperty<>();
  private final ObjectProperty<MapPoolBean> mapPool = new SimpleObjectProperty<>();

  public double getMinRating() {
    return minRating.get();
  }

  public DoubleProperty minRatingProperty() {
    return minRating;
  }

  public void setMinRating(double minRating) {
    this.minRating.set(minRating);
  }

  public double getMaxRating() {
    return maxRating.get();
  }

  public DoubleProperty maxRatingProperty() {
    return maxRating;
  }

  public void setMaxRating(double maxRating) {
    this.maxRating.set(maxRating);
  }

  public MatchmakerQueueBean getMatchmakerQueue() {
    return matchmakerQueue.get();
  }

  public ObjectProperty<MatchmakerQueueBean> matchmakerQueueProperty() {
    return matchmakerQueue;
  }

  public void setMatchmakerQueue(MatchmakerQueueBean matchmakerQueue) {
    this.matchmakerQueue.set(matchmakerQueue);
  }

  public MapPoolBean getMapPool() {
    return mapPool.get();
  }

  public ObjectProperty<MapPoolBean> mapPoolProperty() {
    return mapPool;
  }

  public void setMapPool(MapPoolBean mapPool) {
    this.mapPool.set(mapPool);
  }
}
