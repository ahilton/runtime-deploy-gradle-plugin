class DeployableComponent {

    final String name

    Boolean isDeployable

    // Main deployable artifact
    String id
    String group
    String version
    String extension
    String classifier

    DeployableComponent(String name) {
        this.name = name
    }
}
