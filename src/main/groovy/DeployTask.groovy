import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

class DeployTask extends DefaultTask {

    // https://docs.gradle.org/4.6/userguide/lazy_configuration.html
    @Input
    final Property<DeployableComponent> component = project.objects.property(DeployableComponent)

    @TaskAction
    void performComponentDeploy() {
        def c = component.get()
        println " >>> Task deploy. component name: $c.name. group: $c.group"

        def configName = "configuration$c.name"
        def config = project.configurations.create(configName)
        project.dependencies.add(configName,"com.google.guava:guava:18.0")
        def fs = config.resolve()
        println fs

    }
}