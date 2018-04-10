package dsl

class ArtifactDescriptor {

    final String name

    String group
    String version
    String extension
    String classifier

    ArtifactDescriptor(String name) {
        this.name = name
    }

    /*
        group:id:version:classifier@extension
     */
    def buildDependencyNotation() {
        def extensionNotation = extension == null ? '' : "@$extension"
        def classifierNotation = classifier == null ? '' : classifier
        return "$group:$name:$version:$classifierNotation$extensionNotation"
    }
}
