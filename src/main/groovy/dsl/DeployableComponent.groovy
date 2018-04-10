package dsl

import org.gradle.api.NamedDomainObjectContainer

class DeployableComponent extends ArtifactDescriptor{

    Boolean isDeployable

    NamedDomainObjectContainer<ArtifactDescriptor> dependencies

    def dependencies(final Closure configClosure){
        dependencies.configure(configClosure)
    }

    DeployableComponent(String name) {
        super(name)
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
