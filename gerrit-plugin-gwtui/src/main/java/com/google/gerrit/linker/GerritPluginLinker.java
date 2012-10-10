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
DECL|package|com.google.gerrit.linker
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|linker
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|ext
operator|.
name|LinkerContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwt
operator|.
name|core
operator|.
name|linker
operator|.
name|CrossSiteIframeLinker
import|;
end_import

begin_comment
comment|/** Finalizes the module manifest file with the selection script. */
end_comment

begin_class
DECL|class|GerritPluginLinker
specifier|public
specifier|final
class|class
name|GerritPluginLinker
extends|extends
name|CrossSiteIframeLinker
block|{
annotation|@
name|Override
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
literal|"Gerrit GWT UI plugin"
return|;
block|}
annotation|@
name|Override
DECL|method|getJsComputeUrlForResource (LinkerContext context)
specifier|protected
name|String
name|getJsComputeUrlForResource
parameter_list|(
name|LinkerContext
name|context
parameter_list|)
block|{
return|return
literal|"com/google/gerrit/linker/computeUrlForPluginResource.js"
return|;
block|}
block|}
end_class

end_unit

