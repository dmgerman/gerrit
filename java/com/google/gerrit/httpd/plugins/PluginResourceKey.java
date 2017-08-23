begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.plugins
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|plugins
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
name|httpd
operator|.
name|resources
operator|.
name|ResourceKey
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
name|server
operator|.
name|plugins
operator|.
name|Plugin
import|;
end_import

begin_class
annotation|@
name|AutoValue
DECL|class|PluginResourceKey
specifier|abstract
class|class
name|PluginResourceKey
implements|implements
name|ResourceKey
block|{
DECL|method|create (Plugin p, String r)
specifier|static
name|PluginResourceKey
name|create
parameter_list|(
name|Plugin
name|p
parameter_list|,
name|String
name|r
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_PluginResourceKey
argument_list|(
name|p
operator|.
name|getCacheKey
argument_list|()
argument_list|,
name|r
argument_list|)
return|;
block|}
DECL|method|plugin ()
specifier|public
specifier|abstract
name|Plugin
operator|.
name|CacheKey
name|plugin
parameter_list|()
function_decl|;
DECL|method|resource ()
specifier|public
specifier|abstract
name|String
name|resource
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|weigh ()
specifier|public
name|int
name|weigh
parameter_list|()
block|{
return|return
name|resource
argument_list|()
operator|.
name|length
argument_list|()
operator|*
literal|2
return|;
block|}
block|}
end_class

end_unit

