begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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
comment|/**  * Key-value pair for custom metadata that is provided by plugins.  *  *<p>PluginMetadata allows plugins to include custom metadata into the {@link Metadata} instances  * that are provided as context for performance tracing.  *  *<p>Plugins should use PluginMetadata only for metadata kinds that are not known to Gerrit core  * (metadata for which {@link Metadata} doesn't have a dedicated field).  */
end_comment

begin_class
annotation|@
name|AutoValue
DECL|class|PluginMetadata
specifier|public
specifier|abstract
class|class
name|PluginMetadata
block|{
DECL|method|create (String key, @Nullable String value)
specifier|public
specifier|static
name|PluginMetadata
name|create
parameter_list|(
name|String
name|key
parameter_list|,
annotation|@
name|Nullable
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PluginMetadata
argument_list|(
name|key
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
DECL|method|key ()
specifier|public
specifier|abstract
name|String
name|key
parameter_list|()
function_decl|;
DECL|method|value ()
specifier|public
specifier|abstract
name|Optional
argument_list|<
name|String
argument_list|>
name|value
parameter_list|()
function_decl|;
block|}
end_class

end_unit

