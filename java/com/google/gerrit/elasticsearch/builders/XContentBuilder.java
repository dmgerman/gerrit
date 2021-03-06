begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project, 2009-2015 Elasticsearch
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Licensed under the Apache License, Version 2.0 (the "License");
end_comment

begin_comment
comment|// you may not use this file except in compliance with the License.
end_comment

begin_comment
comment|// You may obtain a copy of the License at
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// http://www.apache.org/licenses/LICENSE-2.0
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Unless required by applicable law or agreed to in writing, software
end_comment

begin_comment
comment|// distributed under the License is distributed on an "AS IS" BASIS,
end_comment

begin_comment
comment|// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
end_comment

begin_comment
comment|// See the License for the specific language governing permissions and
end_comment

begin_comment
comment|// limitations under the License.
end_comment

begin_package
DECL|package|com.google.gerrit.elasticsearch.builders
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|elasticsearch
operator|.
name|builders
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|time
operator|.
name|format
operator|.
name|DateTimeFormatter
operator|.
name|ISO_INSTANT
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonEncoding
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonFactory
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonGenerator
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|json
operator|.
name|JsonReadFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|json
operator|.
name|JsonWriteFeature
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Charsets
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_comment
comment|/** A trimmed down and modified version of org.elasticsearch.common.xcontent.XContentBuilder. */
end_comment

begin_class
DECL|class|XContentBuilder
specifier|public
specifier|final
class|class
name|XContentBuilder
implements|implements
name|Closeable
block|{
DECL|field|generator
specifier|private
specifier|final
name|JsonGenerator
name|generator
decl_stmt|;
DECL|field|bos
specifier|private
specifier|final
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
comment|/**    * Constructs a new builder. Make sure to call {@link #close()} when the builder is done with.    * Inspired from org.elasticsearch.common.xcontent.json.JsonXContent static block.    */
DECL|method|XContentBuilder ()
specifier|public
name|XContentBuilder
parameter_list|()
throws|throws
name|IOException
block|{
name|this
operator|.
name|generator
operator|=
name|JsonFactory
operator|.
name|builder
argument_list|()
operator|.
name|configure
argument_list|(
name|JsonReadFeature
operator|.
name|ALLOW_UNQUOTED_FIELD_NAMES
argument_list|,
literal|true
argument_list|)
operator|.
name|configure
argument_list|(
name|JsonWriteFeature
operator|.
name|QUOTE_FIELD_NAMES
argument_list|,
literal|true
argument_list|)
operator|.
name|configure
argument_list|(
name|JsonReadFeature
operator|.
name|ALLOW_JAVA_COMMENTS
argument_list|,
literal|true
argument_list|)
operator|.
name|configure
argument_list|(
name|JsonFactory
operator|.
name|Feature
operator|.
name|FAIL_ON_SYMBOL_HASH_OVERFLOW
argument_list|,
literal|false
argument_list|)
operator|.
name|build
argument_list|()
operator|.
name|createGenerator
argument_list|(
name|bos
argument_list|,
name|JsonEncoding
operator|.
name|UTF8
argument_list|)
expr_stmt|;
block|}
DECL|method|startObject (String name)
specifier|public
name|XContentBuilder
name|startObject
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|field
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|startObject
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|startObject ()
specifier|public
name|XContentBuilder
name|startObject
parameter_list|()
throws|throws
name|IOException
block|{
name|generator
operator|.
name|writeStartObject
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|endObject ()
specifier|public
name|XContentBuilder
name|endObject
parameter_list|()
throws|throws
name|IOException
block|{
name|generator
operator|.
name|writeEndObject
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|startArray (String name)
specifier|public
name|void
name|startArray
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|field
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|startArray
argument_list|()
expr_stmt|;
block|}
DECL|method|startArray ()
specifier|private
name|void
name|startArray
parameter_list|()
throws|throws
name|IOException
block|{
name|generator
operator|.
name|writeStartArray
argument_list|()
expr_stmt|;
block|}
DECL|method|endArray ()
specifier|public
name|void
name|endArray
parameter_list|()
throws|throws
name|IOException
block|{
name|generator
operator|.
name|writeEndArray
argument_list|()
expr_stmt|;
block|}
DECL|method|field (String name)
specifier|public
name|XContentBuilder
name|field
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|generator
operator|.
name|writeFieldName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|field (String name, String value)
specifier|public
name|XContentBuilder
name|field
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|field
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|generator
operator|.
name|writeString
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|field (String name, int value)
specifier|public
name|XContentBuilder
name|field
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|field
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|generator
operator|.
name|writeNumber
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|field (String name, Iterable<?> value)
specifier|public
name|XContentBuilder
name|field
parameter_list|(
name|String
name|name
parameter_list|,
name|Iterable
argument_list|<
name|?
argument_list|>
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|startArray
argument_list|(
name|name
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|value
control|)
block|{
name|value
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
name|endArray
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|field (String name, Object value)
specifier|public
name|XContentBuilder
name|field
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|field
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|writeValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|value (Object value)
specifier|public
name|XContentBuilder
name|value
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|writeValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|field (String name, boolean value)
specifier|public
name|XContentBuilder
name|field
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|field
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|generator
operator|.
name|writeBoolean
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|value (String value)
specifier|public
name|XContentBuilder
name|value
parameter_list|(
name|String
name|value
parameter_list|)
throws|throws
name|IOException
block|{
name|generator
operator|.
name|writeString
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
try|try
block|{
name|generator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
comment|/** Returns a string representation of the builder (only applicable for text based xcontent). */
DECL|method|string ()
specifier|public
name|String
name|string
parameter_list|()
block|{
name|close
argument_list|()
expr_stmt|;
name|byte
index|[]
name|bytesArray
init|=
name|bos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
operator|new
name|String
argument_list|(
name|bytesArray
argument_list|,
name|Charsets
operator|.
name|UTF_8
argument_list|)
return|;
block|}
DECL|method|writeValue (Object value)
specifier|private
name|void
name|writeValue
parameter_list|(
name|Object
name|value
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|generator
operator|.
name|writeNull
argument_list|()
expr_stmt|;
return|return;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|String
operator|.
name|class
condition|)
block|{
name|generator
operator|.
name|writeString
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|Integer
operator|.
name|class
condition|)
block|{
name|generator
operator|.
name|writeNumber
argument_list|(
operator|(
operator|(
name|Integer
operator|)
name|value
operator|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|type
operator|==
name|byte
index|[]
operator|.
name|class
condition|)
block|{
name|generator
operator|.
name|writeBinary
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Date
condition|)
block|{
name|generator
operator|.
name|writeString
argument_list|(
name|ISO_INSTANT
operator|.
name|format
argument_list|(
operator|(
operator|(
name|Date
operator|)
name|value
operator|)
operator|.
name|toInstant
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if this is a "value" object, like enum, DistanceUnit, ..., just toString it
comment|// yea, it can be misleading when toString a Java class, but really, jackson should be used in
comment|// that case
name|generator
operator|.
name|writeString
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// throw new ElasticsearchIllegalArgumentException("type not supported for generic value
comment|// conversion: " + type);
block|}
block|}
block|}
end_class

end_unit

