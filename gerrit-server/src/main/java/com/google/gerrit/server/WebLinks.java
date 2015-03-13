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
name|common
operator|.
name|base
operator|.
name|Function
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
name|Predicate
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
name|Strings
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
name|collect
operator|.
name|FluentIterable
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
name|common
operator|.
name|DiffWebLinkInfo
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
name|common
operator|.
name|WebLinkInfo
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
name|DiffWebLink
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
name|gerrit
operator|.
name|extensions
operator|.
name|webui
operator|.
name|WebLink
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
name|reviewdb
operator|.
name|client
operator|.
name|Project
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
name|Inject
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
name|Singleton
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
annotation|@
name|Singleton
DECL|class|WebLinks
specifier|public
class|class
name|WebLinks
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WebLinks
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|INVALID_WEBLINK
specifier|private
specifier|static
specifier|final
name|Predicate
argument_list|<
name|WebLinkInfo
argument_list|>
name|INVALID_WEBLINK
init|=
operator|new
name|Predicate
argument_list|<
name|WebLinkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|apply
parameter_list|(
name|WebLinkInfo
name|link
parameter_list|)
block|{
if|if
condition|(
name|link
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|link
operator|.
name|name
argument_list|)
operator|||
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|link
operator|.
name|url
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s is missing name and/or url"
argument_list|,
name|link
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
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
DECL|field|diffLinks
specifier|private
specifier|final
name|DynamicSet
argument_list|<
name|DiffWebLink
argument_list|>
name|diffLinks
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
DECL|method|WebLinks (DynamicSet<PatchSetWebLink> patchSetLinks, DynamicSet<FileWebLink> fileLinks, DynamicSet<DiffWebLink> diffLinks, DynamicSet<ProjectWebLink> projectLinks, DynamicSet<BranchWebLink> branchLinks)
specifier|public
name|WebLinks
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
name|DiffWebLink
argument_list|>
name|diffLinks
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
name|diffLinks
operator|=
name|diffLinks
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
comment|/**    *    * @param project Project name.    * @param commit SHA1 of commit.    * @return Links for patch sets.    */
DECL|method|getPatchSetLinks (final Project.NameKey project, final String commit)
specifier|public
name|FluentIterable
argument_list|<
name|WebLinkInfo
argument_list|>
name|getPatchSetLinks
parameter_list|(
specifier|final
name|Project
operator|.
name|NameKey
name|project
parameter_list|,
specifier|final
name|String
name|commit
parameter_list|)
block|{
return|return
name|filterLinks
argument_list|(
name|patchSetLinks
argument_list|,
operator|new
name|Function
argument_list|<
name|WebLink
argument_list|,
name|WebLinkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|WebLinkInfo
name|apply
parameter_list|(
name|WebLink
name|webLink
parameter_list|)
block|{
return|return
operator|(
operator|(
name|PatchSetWebLink
operator|)
name|webLink
operator|)
operator|.
name|getPatchSetWebLink
argument_list|(
name|project
operator|.
name|get
argument_list|()
argument_list|,
name|commit
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    *    * @param project Project name.    * @param revision SHA1 of revision.    * @param file File name.    * @return Links for files.    */
DECL|method|getFileLinks (final String project, final String revision, final String file)
specifier|public
name|FluentIterable
argument_list|<
name|WebLinkInfo
argument_list|>
name|getFileLinks
parameter_list|(
specifier|final
name|String
name|project
parameter_list|,
specifier|final
name|String
name|revision
parameter_list|,
specifier|final
name|String
name|file
parameter_list|)
block|{
return|return
name|filterLinks
argument_list|(
name|fileLinks
argument_list|,
operator|new
name|Function
argument_list|<
name|WebLink
argument_list|,
name|WebLinkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|WebLinkInfo
name|apply
parameter_list|(
name|WebLink
name|webLink
parameter_list|)
block|{
return|return
operator|(
operator|(
name|FileWebLink
operator|)
name|webLink
operator|)
operator|.
name|getFileWebLink
argument_list|(
name|project
argument_list|,
name|revision
argument_list|,
name|file
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    *    * @param project Project name.    * @param patchSetIdA Patch set ID of side A,<code>null</code> if no base    *        patch set was selected.    * @param revisionA SHA1 of revision of side A.    * @param fileA File name of side A.    * @param patchSetIdB Patch set ID of side B.    * @param revisionB SHA1 of revision of side B.    * @param fileB File name of side B.    * @return Links for file diffs.    */
DECL|method|getDiffLinks (final String project, final int changeId, final Integer patchSetIdA, final String revisionA, final String fileA, final int patchSetIdB, final String revisionB, final String fileB)
specifier|public
name|FluentIterable
argument_list|<
name|DiffWebLinkInfo
argument_list|>
name|getDiffLinks
parameter_list|(
specifier|final
name|String
name|project
parameter_list|,
specifier|final
name|int
name|changeId
parameter_list|,
specifier|final
name|Integer
name|patchSetIdA
parameter_list|,
specifier|final
name|String
name|revisionA
parameter_list|,
specifier|final
name|String
name|fileA
parameter_list|,
specifier|final
name|int
name|patchSetIdB
parameter_list|,
specifier|final
name|String
name|revisionB
parameter_list|,
specifier|final
name|String
name|fileB
parameter_list|)
block|{
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|diffLinks
argument_list|)
operator|.
name|transform
argument_list|(
operator|new
name|Function
argument_list|<
name|WebLink
argument_list|,
name|DiffWebLinkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|DiffWebLinkInfo
name|apply
parameter_list|(
name|WebLink
name|webLink
parameter_list|)
block|{
return|return
operator|(
operator|(
name|DiffWebLink
operator|)
name|webLink
operator|)
operator|.
name|getDiffLink
argument_list|(
name|project
argument_list|,
name|changeId
argument_list|,
name|patchSetIdA
argument_list|,
name|revisionA
argument_list|,
name|fileA
argument_list|,
name|patchSetIdB
argument_list|,
name|revisionB
argument_list|,
name|fileB
argument_list|)
return|;
block|}
block|}
argument_list|)
operator|.
name|filter
argument_list|(
name|INVALID_WEBLINK
argument_list|)
return|;
block|}
comment|/**    *    * @param project Project name.    * @return Links for projects.    */
DECL|method|getProjectLinks (final String project)
specifier|public
name|FluentIterable
argument_list|<
name|WebLinkInfo
argument_list|>
name|getProjectLinks
parameter_list|(
specifier|final
name|String
name|project
parameter_list|)
block|{
return|return
name|filterLinks
argument_list|(
name|projectLinks
argument_list|,
operator|new
name|Function
argument_list|<
name|WebLink
argument_list|,
name|WebLinkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|WebLinkInfo
name|apply
parameter_list|(
name|WebLink
name|webLink
parameter_list|)
block|{
return|return
operator|(
operator|(
name|ProjectWebLink
operator|)
name|webLink
operator|)
operator|.
name|getProjectWeblink
argument_list|(
name|project
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**    *    * @param project Project name    * @param branch Branch name    * @return Links for branches.    */
DECL|method|getBranchLinks (final String project, final String branch)
specifier|public
name|FluentIterable
argument_list|<
name|WebLinkInfo
argument_list|>
name|getBranchLinks
parameter_list|(
specifier|final
name|String
name|project
parameter_list|,
specifier|final
name|String
name|branch
parameter_list|)
block|{
return|return
name|filterLinks
argument_list|(
name|branchLinks
argument_list|,
operator|new
name|Function
argument_list|<
name|WebLink
argument_list|,
name|WebLinkInfo
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|WebLinkInfo
name|apply
parameter_list|(
name|WebLink
name|webLink
parameter_list|)
block|{
return|return
operator|(
operator|(
name|BranchWebLink
operator|)
name|webLink
operator|)
operator|.
name|getBranchWebLink
argument_list|(
name|project
argument_list|,
name|branch
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|filterLinks (DynamicSet<? extends WebLink> links, Function<WebLink, WebLinkInfo> transformer)
specifier|private
name|FluentIterable
argument_list|<
name|WebLinkInfo
argument_list|>
name|filterLinks
parameter_list|(
name|DynamicSet
argument_list|<
name|?
extends|extends
name|WebLink
argument_list|>
name|links
parameter_list|,
name|Function
argument_list|<
name|WebLink
argument_list|,
name|WebLinkInfo
argument_list|>
name|transformer
parameter_list|)
block|{
return|return
name|FluentIterable
operator|.
name|from
argument_list|(
name|links
argument_list|)
operator|.
name|transform
argument_list|(
name|transformer
argument_list|)
operator|.
name|filter
argument_list|(
name|INVALID_WEBLINK
argument_list|)
return|;
block|}
block|}
end_class

end_unit

