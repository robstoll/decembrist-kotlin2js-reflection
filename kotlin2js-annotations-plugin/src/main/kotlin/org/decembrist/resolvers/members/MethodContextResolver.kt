package org.decembrist.resolvers.members

import com.github.sarahbuisson.kotlinparser.KotlinParser.ClassMemberDeclarationContext
import org.decembrist.domain.Import
import org.decembrist.domain.content.members.Method
import org.decembrist.services.RuleContextService.getAnnotations
import org.decembrist.services.RuleContextService.getFunctionModifiers

class MethodContextResolver(
        private val imports: Collection<Import>) : AbstractMemberContextResolver<Method> {

    override fun resolve(ctx: ClassMemberDeclarationContext): Method {
        val funcContext = ctx.functionDeclaration()
        val annotations = getAnnotations(funcContext, imports)
        val name = funcContext.identifier().text
        val functionModifiers = getFunctionModifiers(funcContext)
        return Method(name, functionModifiers).apply {
            this.annotations += annotations
        }
    }

}