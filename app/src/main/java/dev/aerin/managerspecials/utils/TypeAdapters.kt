package dev.aerin.managerspecials.utils

import android.net.Uri
import com.google.gson.*
import java.lang.reflect.Type

/**
 * Serializes and deserializes [Uri] elements from otherwise-string-defined JSON properties
 */
class UriTypeAdapter : JsonDeserializer<Uri>, JsonSerializer<Uri> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Uri {
        return Uri.parse(json.asString)
    }

    override fun serialize(src: Uri, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
