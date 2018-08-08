begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.logging
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|logging
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableSetMultimap
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
name|flogger
operator|.
name|backend
operator|.
name|Tags
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_comment
comment|/**  * Logging context for Flogger.  *  *<p>To configure this logging context for Flogger set the following system property (also see  * {@link com.google.common.flogger.backend.system.DefaultPlatform}):  *  *<ul>  *<li>{@code  *       flogger.logging_context=com.google.gerrit.server.logging.LoggingContext#getInstance}.  *</ul>  */
end_comment

begin_class
DECL|class|LoggingContext
specifier|public
class|class
name|LoggingContext
extends|extends
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|flogger
operator|.
name|backend
operator|.
name|system
operator|.
name|LoggingContext
block|{
DECL|field|INSTANCE
specifier|private
specifier|static
specifier|final
name|LoggingContext
name|INSTANCE
init|=
operator|new
name|LoggingContext
argument_list|()
decl_stmt|;
DECL|field|tags
specifier|private
specifier|static
specifier|final
name|ThreadLocal
argument_list|<
name|MutableTags
argument_list|>
name|tags
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|LoggingContext ()
specifier|private
name|LoggingContext
parameter_list|()
block|{}
comment|/** This method is expected to be called via reflection (and might otherwise be unused). */
DECL|method|getInstance ()
specifier|public
specifier|static
name|LoggingContext
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
annotation|@
name|Override
DECL|method|shouldForceLogging (String loggerName, Level level, boolean isEnabled)
specifier|public
name|boolean
name|shouldForceLogging
parameter_list|(
name|String
name|loggerName
parameter_list|,
name|Level
name|level
parameter_list|,
name|boolean
name|isEnabled
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|getTags ()
specifier|public
name|Tags
name|getTags
parameter_list|()
block|{
name|MutableTags
name|mutableTags
init|=
name|tags
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|mutableTags
operator|!=
literal|null
condition|?
name|mutableTags
operator|.
name|getTags
argument_list|()
else|:
name|Tags
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|getTagsAsMap ()
specifier|public
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getTagsAsMap
parameter_list|()
block|{
name|MutableTags
name|mutableTags
init|=
name|tags
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|mutableTags
operator|!=
literal|null
condition|?
name|mutableTags
operator|.
name|asMap
argument_list|()
else|:
name|ImmutableSetMultimap
operator|.
name|of
argument_list|()
return|;
block|}
DECL|method|addTag (String name, String value)
name|boolean
name|addTag
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
return|return
name|getMutableTags
argument_list|()
operator|.
name|add
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|removeTag (String name, String value)
name|void
name|removeTag
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MutableTags
name|mutableTags
init|=
name|getMutableTags
argument_list|()
decl_stmt|;
name|mutableTags
operator|.
name|remove
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|mutableTags
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|tags
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|setTags (ImmutableSetMultimap<String, String> newTags)
name|void
name|setTags
parameter_list|(
name|ImmutableSetMultimap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|newTags
parameter_list|)
block|{
if|if
condition|(
name|newTags
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|tags
operator|.
name|remove
argument_list|()
expr_stmt|;
return|return;
block|}
name|getMutableTags
argument_list|()
operator|.
name|set
argument_list|(
name|newTags
argument_list|)
expr_stmt|;
block|}
DECL|method|clearTags ()
name|void
name|clearTags
parameter_list|()
block|{
name|tags
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
DECL|method|getMutableTags ()
specifier|private
name|MutableTags
name|getMutableTags
parameter_list|()
block|{
name|MutableTags
name|mutableTags
init|=
name|tags
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|mutableTags
operator|==
literal|null
condition|)
block|{
name|mutableTags
operator|=
operator|new
name|MutableTags
argument_list|()
expr_stmt|;
name|tags
operator|.
name|set
argument_list|(
name|mutableTags
argument_list|)
expr_stmt|;
block|}
return|return
name|mutableTags
return|;
block|}
block|}
end_class

end_unit

