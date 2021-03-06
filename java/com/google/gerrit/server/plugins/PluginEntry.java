begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2014 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|plugins
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Comparator
operator|.
name|comparing
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_comment
comment|/**  * Plugin static resource entry  *  *<p>Bean representing a static resource inside a plugin. All static resources are available at  * {@code<plugin web url>/static} and served by the HttpPluginServlet.  */
end_comment

begin_class
DECL|class|PluginEntry
specifier|public
class|class
name|PluginEntry
block|{
DECL|field|ATTR_CHARACTER_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|ATTR_CHARACTER_ENCODING
init|=
literal|"Character-Encoding"
decl_stmt|;
DECL|field|ATTR_CONTENT_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|ATTR_CONTENT_TYPE
init|=
literal|"Content-Type"
decl_stmt|;
DECL|field|COMPARATOR_BY_NAME
specifier|public
specifier|static
specifier|final
name|Comparator
argument_list|<
name|PluginEntry
argument_list|>
name|COMPARATOR_BY_NAME
init|=
name|comparing
argument_list|(
name|PluginEntry
operator|::
name|getName
argument_list|)
decl_stmt|;
DECL|field|EMPTY_ATTRS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|EMPTY_ATTRS
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
DECL|field|NO_SIZE
specifier|private
specifier|static
specifier|final
name|Optional
argument_list|<
name|Long
argument_list|>
name|NO_SIZE
init|=
name|Optional
operator|.
name|empty
argument_list|()
decl_stmt|;
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|time
specifier|private
specifier|final
name|long
name|time
decl_stmt|;
DECL|field|size
specifier|private
specifier|final
name|Optional
argument_list|<
name|Long
argument_list|>
name|size
decl_stmt|;
DECL|field|attrs
specifier|private
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|attrs
decl_stmt|;
DECL|method|PluginEntry (String name, long time, Optional<Long> size, Map<Object, String> attrs)
specifier|public
name|PluginEntry
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|time
parameter_list|,
name|Optional
argument_list|<
name|Long
argument_list|>
name|size
parameter_list|,
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|attrs
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|time
operator|=
name|time
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|attrs
operator|=
name|attrs
expr_stmt|;
block|}
DECL|method|PluginEntry (String name, long time, Optional<Long> size)
specifier|public
name|PluginEntry
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|time
parameter_list|,
name|Optional
argument_list|<
name|Long
argument_list|>
name|size
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|time
argument_list|,
name|size
argument_list|,
name|EMPTY_ATTRS
argument_list|)
expr_stmt|;
block|}
DECL|method|PluginEntry (String name, long time)
specifier|public
name|PluginEntry
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|time
parameter_list|)
block|{
name|this
argument_list|(
name|name
argument_list|,
name|time
argument_list|,
name|NO_SIZE
argument_list|,
name|EMPTY_ATTRS
argument_list|)
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getTime ()
specifier|public
name|long
name|getTime
parameter_list|()
block|{
return|return
name|time
return|;
block|}
DECL|method|getSize ()
specifier|public
name|Optional
argument_list|<
name|Long
argument_list|>
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
DECL|method|getAttrs ()
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|String
argument_list|>
name|getAttrs
parameter_list|()
block|{
return|return
name|attrs
return|;
block|}
block|}
end_class

end_unit

