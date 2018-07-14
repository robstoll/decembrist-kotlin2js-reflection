package org.decembrist.generators.annotations

import com.squareup.kotlinpoet.CodeBlock
import org.decembrist.domain.Attribute
import org.decembrist.domain.content.IAnnotated
import org.decembrist.generators.IGenerator
import org.decembrist.generators.nextLine

object AnnotationBlockGenerator: IGenerator<IAnnotated> {

    override fun generate(content: IAnnotated): CodeBlock {
        val annotationsBlock = CodeBlock.builder()
        if (content.annotations.isEmpty()) {
            annotationsBlock
                    .add("emptyList<Annotation>()")
        } else {
            annotationsBlock
                    .add("listOf<Annotation>(")
                    .nextLine()
                    .indent()
            var needComma = false
            for (annotation in content.annotations) {
                if (needComma) annotationsBlock
                        .add(",")
                        .nextLine()
                val annotationType = annotation.type.toClassName()
                val attributesBlock = makeAttributes(annotation.attributes)
                annotationsBlock
                        .add("%T::class.jsReflect.createInstance(", annotationType)
                        .add(attributesBlock)
                        .add(")")
                needComma = true
            }
            annotationsBlock
                    .nextLine()
                    .unindent()
                    .add(")")
        }
        return annotationsBlock.build()
    }

    private fun makeAttributes(attributes: MutableList<Attribute>): CodeBlock {
        val result = CodeBlock.builder().add("arrayOf(")
        val attributesString = attributes.joinToString { attribute ->
            if (attribute.type.type == "String") "\"${attribute.value}\"" else attribute.value
        }
        result.add(attributesString)
        result.add(")")
        return result.build()
    }

}