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
name|gerrit
operator|.
name|common
operator|.
name|errors
operator|.
name|EmailException
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
name|api
operator|.
name|changes
operator|.
name|RevertInput
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
name|BadRequestException
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
name|extensions
operator|.
name|webui
operator|.
name|UiAction
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
name|Change
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
name|Change
operator|.
name|Status
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
name|ChangeUtil
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
name|GerritPersonIdent
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
name|change
operator|.
name|ChangeJson
operator|.
name|ChangeInfo
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
name|ChangeControl
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
name|NoSuchChangeException
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
name|ssh
operator|.
name|NoSshInfo
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|lib
operator|.
name|PersonIdent
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

begin_class
annotation|@
name|Singleton
DECL|class|Revert
specifier|public
class|class
name|Revert
implements|implements
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|RevertInput
argument_list|>
implements|,
name|UiAction
argument_list|<
name|ChangeResource
argument_list|>
block|{
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
name|json
decl_stmt|;
DECL|field|changeUtil
specifier|private
specifier|final
name|ChangeUtil
name|changeUtil
decl_stmt|;
DECL|field|myIdent
specifier|private
specifier|final
name|PersonIdent
name|myIdent
decl_stmt|;
annotation|@
name|Inject
DECL|method|Revert (ChangeJson json, ChangeUtil changeUtil, @GerritPersonIdent PersonIdent myIdent)
name|Revert
parameter_list|(
name|ChangeJson
name|json
parameter_list|,
name|ChangeUtil
name|changeUtil
parameter_list|,
annotation|@
name|GerritPersonIdent
name|PersonIdent
name|myIdent
parameter_list|)
block|{
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|this
operator|.
name|changeUtil
operator|=
name|changeUtil
expr_stmt|;
name|this
operator|.
name|myIdent
operator|=
name|myIdent
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource req, RevertInput input)
specifier|public
name|ChangeInfo
name|apply
parameter_list|(
name|ChangeResource
name|req
parameter_list|,
name|RevertInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|BadRequestException
throws|,
name|ResourceConflictException
throws|,
name|ResourceNotFoundException
throws|,
name|IOException
throws|,
name|OrmException
throws|,
name|EmailException
block|{
name|ChangeControl
name|control
init|=
name|req
operator|.
name|getControl
argument_list|()
decl_stmt|;
name|Change
name|change
init|=
name|req
operator|.
name|getChange
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|control
operator|.
name|canAddPatchSet
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"revert not permitted"
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|change
operator|.
name|getStatus
argument_list|()
operator|!=
name|Status
operator|.
name|MERGED
condition|)
block|{
throw|throw
operator|new
name|ResourceConflictException
argument_list|(
literal|"change is "
operator|+
name|status
argument_list|(
name|change
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
name|Change
operator|.
name|Id
name|revertedChangeId
init|=
name|changeUtil
operator|.
name|revert
argument_list|(
name|control
argument_list|,
name|change
operator|.
name|currentPatchSetId
argument_list|()
argument_list|,
name|Strings
operator|.
name|emptyToNull
argument_list|(
name|input
operator|.
name|message
argument_list|)
argument_list|,
name|myIdent
argument_list|,
operator|new
name|NoSshInfo
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|json
operator|.
name|format
argument_list|(
name|revertedChangeId
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
name|BadRequestException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchChangeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceNotFoundException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDescription (ChangeResource resource)
specifier|public
name|UiAction
operator|.
name|Description
name|getDescription
parameter_list|(
name|ChangeResource
name|resource
parameter_list|)
block|{
return|return
operator|new
name|UiAction
operator|.
name|Description
argument_list|()
operator|.
name|setLabel
argument_list|(
literal|"Revert"
argument_list|)
operator|.
name|setTitle
argument_list|(
literal|"Revert the change"
argument_list|)
operator|.
name|setVisible
argument_list|(
name|resource
operator|.
name|getChange
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|==
name|Status
operator|.
name|MERGED
operator|&&
name|resource
operator|.
name|getControl
argument_list|()
operator|.
name|getRefControl
argument_list|()
operator|.
name|canUpload
argument_list|()
argument_list|)
return|;
block|}
DECL|method|status (Change change)
specifier|private
specifier|static
name|String
name|status
parameter_list|(
name|Change
name|change
parameter_list|)
block|{
return|return
name|change
operator|!=
literal|null
condition|?
name|change
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
else|:
literal|"deleted"
return|;
block|}
block|}
end_class

end_unit

