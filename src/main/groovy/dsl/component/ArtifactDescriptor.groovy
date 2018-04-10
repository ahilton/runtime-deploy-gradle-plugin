package dsl.component

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
     *   group:id:version:classifier@extension
     */
    def buildDependencyNotation() {
        def extensionNotation = extension == null ? '' : "@$extension"
        def classifierNotation = classifier == null ? '' : classifier
        return "$group:$name:$version:$classifierNotation$extensionNotation"
    }

    /*
     *  Convert kebab-case into capitalized CamelCase for use as a gradle identifier:
     *
     *      taskCamelCase
     *      configurationCamelCase
     */
    String getNameAsGradleCompatibleIdentifier(){
        return name.replaceAll(/-\w/){ it[1].toUpperCase() }.capitalize()
    }
}
