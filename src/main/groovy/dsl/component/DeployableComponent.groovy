package dsl.component

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
}
