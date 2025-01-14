package com.faforever.client.domain;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FeaturedModBean {

  @EqualsAndHashCode.Include
  private final ObjectProperty<Integer> id = new SimpleObjectProperty<>();
  private final StringProperty technicalName = new SimpleStringProperty();
  private final StringProperty displayName = new SimpleStringProperty();
  private final StringProperty description = new SimpleStringProperty();
  private final StringProperty gitUrl = new SimpleStringProperty();
  private final StringProperty gitBranch = new SimpleStringProperty();
  private final BooleanProperty visible = new SimpleBooleanProperty();

  public String getTechnicalName() {
    return technicalName.get();
  }

  public void setTechnicalName(String technicalName) {
    this.technicalName.set(technicalName);
  }

  public StringProperty technicalNameProperty() {
    return technicalName;
  }

  public String getDisplayName() {
    return displayName.get();
  }

  public void setDisplayName(String displayName) {
    this.displayName.set(displayName);
  }

  public StringProperty displayNameProperty() {
    return displayName;
  }

  public String getDescription() {
    return description.get();
  }

  public void setDescription(String description) {
    this.description.set(description);
  }

  public StringProperty descriptionProperty() {
    return description;
  }

  public boolean getVisible() {
    return visible.get();
  }

  public void setVisible(boolean visible) {
    this.visible.set(visible);
  }

  public BooleanProperty visibleProperty() {
    return visible;
  }

  public String getGitUrl() {
    return gitUrl.get();
  }

  public void setGitUrl(String gitUrl) {
    this.gitUrl.set(gitUrl);
  }

  public StringProperty gitUrlProperty() {
    return gitUrl;
  }

  public String getGitBranch() {
    return gitBranch.get();
  }

  public void setGitBranch(String gitBranch) {
    this.gitBranch.set(gitBranch);
  }

  public StringProperty gitBranchProperty() {
    return gitBranch;
  }

  public Integer getId() {
    return id.get();
  }

  public void setId(Integer id) {
    this.id.set(id);
  }

  public ObjectProperty<Integer> idProperty() {
    return id;
  }
}
