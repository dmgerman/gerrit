begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.change
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|change
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
name|common
operator|.
name|EditInfo
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
name|restapi
operator|.
name|AuthException
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
name|restapi
operator|.
name|ResourceConflictException
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
name|restapi
operator|.
name|ResourceNotFoundException
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
name|restapi
operator|.
name|Response
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
name|restapi
operator|.
name|RestModifyView
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
name|PatchSet
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
name|gerrit
operator|.
name|server
operator|.
name|edit
operator|.
name|ChangeEdit
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
name|edit
operator|.
name|ChangeEditJson
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
name|edit
operator|.
name|ChangeEditModifier
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
name|edit
operator|.
name|tree
operator|.
name|TreeModification
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
name|fixes
operator|.
name|FixReplacementInterpreter
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
name|git
operator|.
name|GitRepositoryManager
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
name|permissions
operator|.
name|PermissionBackendException
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
name|InvalidChangeOperationException
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
name|ProjectState
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
name|server
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
name|Singleton
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
name|List
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
name|ObjectId
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

begin_class
annotation|@
name|Singleton
DECL|class|ApplyFix
specifier|public
class|class
name|ApplyFix
implements|implements
name|RestModifyView
argument_list|<
name|FixResource
argument_list|,
name|Void
argument_list|>
block|{
DECL|field|gitRepositoryManager
specifier|private
specifier|final
name|GitRepositoryManager
name|gitRepositoryManager
decl_stmt|;
DECL|field|fixReplacementInterpreter
specifier|private
specifier|final
name|FixReplacementInterpreter
name|fixReplacementInterpreter
decl_stmt|;
DECL|field|changeEditModifier
specifier|private
specifier|final
name|ChangeEditModifier
name|changeEditModifier
decl_stmt|;
DECL|field|changeEditJson
specifier|private
specifier|final
name|ChangeEditJson
name|changeEditJson
decl_stmt|;
annotation|@
name|Inject
DECL|method|ApplyFix ( GitRepositoryManager gitRepositoryManager, FixReplacementInterpreter fixReplacementInterpreter, ChangeEditModifier changeEditModifier, ChangeEditJson changeEditJson)
specifier|public
name|ApplyFix
parameter_list|(
name|GitRepositoryManager
name|gitRepositoryManager
parameter_list|,
name|FixReplacementInterpreter
name|fixReplacementInterpreter
parameter_list|,
name|ChangeEditModifier
name|changeEditModifier
parameter_list|,
name|ChangeEditJson
name|changeEditJson
parameter_list|)
block|{
name|this
operator|.
name|gitRepositoryManager
operator|=
name|gitRepositoryManager
expr_stmt|;
name|this
operator|.
name|fixReplacementInterpreter
operator|=
name|fixReplacementInterpreter
expr_stmt|;
name|this
operator|.
name|changeEditModifier
operator|=
name|changeEditModifier
expr_stmt|;
name|this
operator|.
name|changeEditJson
operator|=
name|changeEditJson
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (FixResource fixResource, Void nothing)
specifier|public
name|Response
argument_list|<
name|EditInfo
argument_list|>
name|apply
parameter_list|(
name|FixResource
name|fixResource
parameter_list|,
name|Void
name|nothing
parameter_list|)
throws|throws
name|AuthException
throws|,
name|OrmException
throws|,
name|ResourceConflictException
throws|,
name|IOException
throws|,
name|ResourceNotFoundException
throws|,
name|PermissionBackendException
block|{
name|RevisionResource
name|revisionResource
init|=
name|fixResource
operator|.
name|getRevisionResource
argument_list|()
decl_stmt|;
name|Project
operator|.
name|NameKey
name|project
init|=
name|revisionResource
operator|.
name|getProject
argument_list|()
decl_stmt|;
name|ProjectState
name|projectState
init|=
name|revisionResource
operator|.
name|getControl
argument_list|()
operator|.
name|getProjectControl
argument_list|()
operator|.
name|getProjectState
argument_list|()
decl_stmt|;
name|PatchSet
name|patchSet
init|=
name|revisionResource
operator|.
name|getPatchSet
argument_list|()
decl_stmt|;
name|ObjectId
name|patchSetCommitId
init|=
name|ObjectId
operator|.
name|fromString
argument_list|(
name|patchSet
operator|.
name|getRevision
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
decl_stmt|;
try|try
init|(
name|Repository
name|repository
init|=
name|gitRepositoryManager
operator|.
name|openRepository
argument_list|(
name|project
argument_list|)
init|)
block|{
name|List
argument_list|<
name|TreeModification
argument_list|>
name|treeModifications
init|=
name|fixReplacementInterpreter
operator|.
name|toTreeModifications
argument_list|(
name|repository
argument_list|,
name|projectState
argument_list|,
name|patchSetCommitId
argument_list|,
name|fixResource
operator|.
name|getFixReplacements
argument_list|()
argument_list|)
decl_stmt|;
name|ChangeEdit
name|changeEdit
init|=
name|changeEditModifier
operator|.
name|combineWithModifiedPatchSetTree
argument_list|(
name|repository
argument_list|,
name|revisionResource
operator|.
name|getControl
argument_list|()
argument_list|,
name|patchSet
argument_list|,
name|treeModifications
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|changeEditJson
operator|.
name|toEditInfo
argument_list|(
name|changeEdit
argument_list|,
literal|false
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidChangeOperationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

