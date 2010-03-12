begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2010 The Android Open Source Project
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
DECL|package|com.google.gerrit.httpd.rpc.project
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|httpd
operator|.
name|rpc
operator|.
name|project
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
name|common
operator|.
name|data
operator|.
name|ApprovalType
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
name|data
operator|.
name|ApprovalTypes
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
name|data
operator|.
name|ProjectDetail
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
name|errors
operator|.
name|InvalidNameException
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
name|rpc
operator|.
name|Handler
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
name|AccountGroup
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
name|ApprovalCategory
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
name|Project
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
name|RefRight
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
name|ReviewDb
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
name|account
operator|.
name|GroupCache
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
name|account
operator|.
name|NoSuchGroupException
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
name|project
operator|.
name|NoSuchProjectException
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
name|project
operator|.
name|NoSuchRefException
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
name|project
operator|.
name|ProjectCache
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
name|project
operator|.
name|ProjectControl
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
name|project
operator|.
name|RefControl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gwtorm
operator|.
name|client
operator|.
name|OrmException
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
name|assistedinject
operator|.
name|Assisted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|Repository
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
name|javax
operator|.
name|annotation
operator|.
name|Nullable
import|;
end_import

begin_class
DECL|class|AddRefRight
class|class
name|AddRefRight
extends|extends
name|Handler
argument_list|<
name|ProjectDetail
argument_list|>
block|{
DECL|interface|Factory
interface|interface
name|Factory
block|{
DECL|method|create (@ssisted Project.NameKey projectName, @Assisted ApprovalCategory.Id categoryId, @Assisted(R) String groupName, @Nullable @Assisted(R) String refPattern, @Assisted(R) short min, @Assisted(R) short max)
name|AddRefRight
name|create
parameter_list|(
annotation|@
name|Assisted
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
annotation|@
name|Assisted
name|ApprovalCategory
operator|.
name|Id
name|categoryId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"groupName"
argument_list|)
name|String
name|groupName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
argument_list|(
literal|"refPattern"
argument_list|)
name|String
name|refPattern
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"min"
argument_list|)
name|short
name|min
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"max"
argument_list|)
name|short
name|max
parameter_list|)
function_decl|;
block|}
DECL|field|projectDetailFactory
specifier|private
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
decl_stmt|;
DECL|field|projectControlFactory
specifier|private
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
decl_stmt|;
DECL|field|projectCache
specifier|private
specifier|final
name|ProjectCache
name|projectCache
decl_stmt|;
DECL|field|groupCache
specifier|private
specifier|final
name|GroupCache
name|groupCache
decl_stmt|;
DECL|field|db
specifier|private
specifier|final
name|ReviewDb
name|db
decl_stmt|;
DECL|field|approvalTypes
specifier|private
specifier|final
name|ApprovalTypes
name|approvalTypes
decl_stmt|;
DECL|field|projectName
specifier|private
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
decl_stmt|;
DECL|field|categoryId
specifier|private
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|categoryId
decl_stmt|;
DECL|field|groupName
specifier|private
specifier|final
name|AccountGroup
operator|.
name|NameKey
name|groupName
decl_stmt|;
DECL|field|refPattern
specifier|private
specifier|final
name|String
name|refPattern
decl_stmt|;
DECL|field|min
specifier|private
specifier|final
name|short
name|min
decl_stmt|;
DECL|field|max
specifier|private
specifier|final
name|short
name|max
decl_stmt|;
annotation|@
name|Inject
DECL|method|AddRefRight (final ProjectDetailFactory.Factory projectDetailFactory, final ProjectControl.Factory projectControlFactory, final ProjectCache projectCache, final GroupCache groupCache, final ReviewDb db, final ApprovalTypes approvalTypes, @Assisted final Project.NameKey projectName, @Assisted final ApprovalCategory.Id categoryId, @Assisted(R) final String groupName, @Nullable @Assisted(R) final String refPattern, @Assisted(R) final short min, @Assisted(R) final short max)
name|AddRefRight
parameter_list|(
specifier|final
name|ProjectDetailFactory
operator|.
name|Factory
name|projectDetailFactory
parameter_list|,
specifier|final
name|ProjectControl
operator|.
name|Factory
name|projectControlFactory
parameter_list|,
specifier|final
name|ProjectCache
name|projectCache
parameter_list|,
specifier|final
name|GroupCache
name|groupCache
parameter_list|,
specifier|final
name|ReviewDb
name|db
parameter_list|,
specifier|final
name|ApprovalTypes
name|approvalTypes
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|,
annotation|@
name|Assisted
specifier|final
name|ApprovalCategory
operator|.
name|Id
name|categoryId
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"groupName"
argument_list|)
specifier|final
name|String
name|groupName
parameter_list|,
annotation|@
name|Nullable
annotation|@
name|Assisted
argument_list|(
literal|"refPattern"
argument_list|)
specifier|final
name|String
name|refPattern
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"min"
argument_list|)
specifier|final
name|short
name|min
parameter_list|,
annotation|@
name|Assisted
argument_list|(
literal|"max"
argument_list|)
specifier|final
name|short
name|max
parameter_list|)
block|{
name|this
operator|.
name|projectDetailFactory
operator|=
name|projectDetailFactory
expr_stmt|;
name|this
operator|.
name|projectControlFactory
operator|=
name|projectControlFactory
expr_stmt|;
name|this
operator|.
name|projectCache
operator|=
name|projectCache
expr_stmt|;
name|this
operator|.
name|groupCache
operator|=
name|groupCache
expr_stmt|;
name|this
operator|.
name|approvalTypes
operator|=
name|approvalTypes
expr_stmt|;
name|this
operator|.
name|db
operator|=
name|db
expr_stmt|;
name|this
operator|.
name|projectName
operator|=
name|projectName
expr_stmt|;
name|this
operator|.
name|categoryId
operator|=
name|categoryId
expr_stmt|;
name|this
operator|.
name|groupName
operator|=
operator|new
name|AccountGroup
operator|.
name|NameKey
argument_list|(
name|groupName
argument_list|)
expr_stmt|;
name|this
operator|.
name|refPattern
operator|=
name|refPattern
operator|!=
literal|null
condition|?
name|refPattern
operator|.
name|trim
argument_list|()
else|:
literal|null
expr_stmt|;
if|if
condition|(
name|min
operator|<=
name|max
condition|)
block|{
name|this
operator|.
name|min
operator|=
name|min
expr_stmt|;
name|this
operator|.
name|max
operator|=
name|max
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|min
operator|=
name|max
expr_stmt|;
name|this
operator|.
name|max
operator|=
name|min
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|ProjectDetail
name|call
parameter_list|()
throws|throws
name|NoSuchProjectException
throws|,
name|OrmException
throws|,
name|NoSuchGroupException
throws|,
name|InvalidNameException
throws|,
name|NoSuchRefException
block|{
specifier|final
name|ProjectControl
name|projectControl
init|=
name|projectControlFactory
operator|.
name|controlFor
argument_list|(
name|projectName
argument_list|)
decl_stmt|;
specifier|final
name|ApprovalType
name|at
init|=
name|approvalTypes
operator|.
name|getApprovalType
argument_list|(
name|categoryId
argument_list|)
decl_stmt|;
if|if
condition|(
name|at
operator|==
literal|null
operator|||
name|at
operator|.
name|getValue
argument_list|(
name|min
argument_list|)
operator|==
literal|null
operator|||
name|at
operator|.
name|getValue
argument_list|(
name|max
argument_list|)
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid category "
operator|+
name|categoryId
operator|+
literal|" or range "
operator|+
name|min
operator|+
literal|".."
operator|+
name|max
argument_list|)
throw|;
block|}
name|String
name|refPattern
init|=
name|this
operator|.
name|refPattern
decl_stmt|;
if|if
condition|(
name|refPattern
operator|==
literal|null
operator|||
name|refPattern
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|categoryId
operator|.
name|equals
argument_list|(
name|ApprovalCategory
operator|.
name|SUBMIT
argument_list|)
operator|||
name|categoryId
operator|.
name|equals
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_HEAD
argument_list|)
condition|)
block|{
comment|// Explicitly related to a branch head.
name|refPattern
operator|=
name|Constants
operator|.
name|R_HEADS
operator|+
literal|"*"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|at
operator|.
name|getCategory
argument_list|()
operator|.
name|isAction
argument_list|()
condition|)
block|{
comment|// Non actions are approval votes on a change, assume these apply
comment|// to branch heads only.
name|refPattern
operator|=
name|Constants
operator|.
name|R_HEADS
operator|+
literal|"*"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|categoryId
operator|.
name|equals
argument_list|(
name|ApprovalCategory
operator|.
name|PUSH_TAG
argument_list|)
condition|)
block|{
comment|// Explicitly related to the tag namespace.
name|refPattern
operator|=
name|Constants
operator|.
name|R_TAGS
operator|+
literal|"*"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|categoryId
operator|.
name|equals
argument_list|(
name|ApprovalCategory
operator|.
name|READ
argument_list|)
operator|||
name|categoryId
operator|.
name|equals
argument_list|(
name|ApprovalCategory
operator|.
name|OWN
argument_list|)
condition|)
block|{
comment|// Currently these are project-wide rights, so apply that way.
name|refPattern
operator|=
name|RefRight
operator|.
name|ALL
expr_stmt|;
block|}
else|else
block|{
comment|// Assume project wide for the default.
name|refPattern
operator|=
name|RefRight
operator|.
name|ALL
expr_stmt|;
block|}
block|}
while|while
condition|(
name|refPattern
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|refPattern
operator|=
name|refPattern
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|refPattern
operator|.
name|startsWith
argument_list|(
name|Constants
operator|.
name|R_REFS
argument_list|)
condition|)
block|{
name|refPattern
operator|=
name|Constants
operator|.
name|R_HEADS
operator|+
name|refPattern
expr_stmt|;
block|}
if|if
condition|(
name|refPattern
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
specifier|final
name|String
name|prefix
init|=
name|refPattern
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|refPattern
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"refs"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
operator|&&
operator|!
name|Repository
operator|.
name|isValidRefName
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidNameException
argument_list|()
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|Repository
operator|.
name|isValidRefName
argument_list|(
name|refPattern
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|InvalidNameException
argument_list|()
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|controlForRef
argument_list|(
name|projectControl
argument_list|,
name|refPattern
argument_list|)
operator|.
name|isOwner
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|NoSuchRefException
argument_list|(
name|refPattern
argument_list|)
throw|;
block|}
specifier|final
name|AccountGroup
name|group
init|=
name|groupCache
operator|.
name|get
argument_list|(
name|groupName
argument_list|)
decl_stmt|;
if|if
condition|(
name|group
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchGroupException
argument_list|(
name|groupName
argument_list|)
throw|;
block|}
specifier|final
name|RefRight
operator|.
name|Key
name|key
init|=
operator|new
name|RefRight
operator|.
name|Key
argument_list|(
name|projectName
argument_list|,
operator|new
name|RefRight
operator|.
name|RefPattern
argument_list|(
name|refPattern
argument_list|)
argument_list|,
name|categoryId
argument_list|,
name|group
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|RefRight
name|rr
init|=
name|db
operator|.
name|refRights
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|rr
operator|==
literal|null
condition|)
block|{
name|rr
operator|=
operator|new
name|RefRight
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|rr
operator|.
name|setMinValue
argument_list|(
name|min
argument_list|)
expr_stmt|;
name|rr
operator|.
name|setMaxValue
argument_list|(
name|max
argument_list|)
expr_stmt|;
name|db
operator|.
name|refRights
argument_list|()
operator|.
name|insert
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|rr
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rr
operator|.
name|setMinValue
argument_list|(
name|min
argument_list|)
expr_stmt|;
name|rr
operator|.
name|setMaxValue
argument_list|(
name|max
argument_list|)
expr_stmt|;
name|db
operator|.
name|refRights
argument_list|()
operator|.
name|update
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|rr
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|projectCache
operator|.
name|evict
argument_list|(
name|projectControl
operator|.
name|getProject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|projectDetailFactory
operator|.
name|create
argument_list|(
name|projectName
argument_list|)
operator|.
name|call
argument_list|()
return|;
block|}
DECL|method|controlForRef (ProjectControl p, String ref)
specifier|private
name|RefControl
name|controlForRef
parameter_list|(
name|ProjectControl
name|p
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
if|if
condition|(
name|ref
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
name|ref
operator|=
name|ref
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ref
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|p
operator|.
name|controlForRef
argument_list|(
name|ref
argument_list|)
return|;
block|}
block|}
end_class

end_unit

