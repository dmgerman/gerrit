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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|registration
operator|.
name|DynamicSet
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
name|extensions
operator|.
name|webui
operator|.
name|BranchWebLink
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
name|extensions
operator|.
name|webui
operator|.
name|FileWebLink
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
name|extensions
operator|.
name|webui
operator|.
name|PatchSetWebLink
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
name|extensions
operator|.
name|webui
operator|.
name|ProjectWebLink
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|inject
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_class
DECL|class|WebLinksProvider
specifier|public
class|class
name|WebLinksProvider
implements|implements
name|Provider
argument_list|<
name|WebLinks
argument_list|>
block|{
DECL|field|patchSetLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|PatchSetWebLink
argument_list|>
name|patchSetLinks
decl_stmt|;
DECL|field|fileLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|FileWebLink
argument_list|>
name|fileLinks
decl_stmt|;
DECL|field|projectLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|ProjectWebLink
argument_list|>
name|projectLinks
decl_stmt|;
DECL|field|branchLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|BranchWebLink
argument_list|>
name|branchLinks
decl_stmt|;
annotation|@
name|Inject
DECL|method|WebLinksProvider (DynamicSet<PatchSetWebLink> patchSetLinks, DynamicSet<FileWebLink> fileLinks, DynamicSet<ProjectWebLink> projectLinks, DynamicSet<BranchWebLink> branchLinks)
specifier|public
name|WebLinksProvider
parameter_list|(
name|DynamicSet
argument_list|<
name|PatchSetWebLink
argument_list|>
name|patchSetLinks
parameter_list|,
name|DynamicSet
argument_list|<
name|FileWebLink
argument_list|>
name|fileLinks
parameter_list|,
name|DynamicSet
argument_list|<
name|ProjectWebLink
argument_list|>
name|projectLinks
parameter_list|,
name|DynamicSet
argument_list|<
name|BranchWebLink
argument_list|>
name|branchLinks
parameter_list|)
block|{
name|this
operator|.
name|patchSetLinks
operator|=
name|patchSetLinks
expr_stmt|;
name|this
operator|.
name|fileLinks
operator|=
name|fileLinks
expr_stmt|;
name|this
operator|.
name|projectLinks
operator|=
name|projectLinks
expr_stmt|;
name|this
operator|.
name|branchLinks
operator|=
name|branchLinks
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|get ()
specifier|public
name|WebLinks
name|get
parameter_list|()
block|{
return|return
operator|new
name|WebLinks
argument_list|(
name|patchSetLinks
argument_list|,
name|fileLinks
argument_list|,
name|projectLinks
argument_list|,
name|branchLinks
argument_list|)
return|;
block|}
block|}
end_class

end_unit

