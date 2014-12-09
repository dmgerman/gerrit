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
name|api
operator|.
name|changes
operator|.
name|FixInput
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
name|extensions
operator|.
name|common
operator|.
name|ListChangesOption
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
name|extensions
operator|.
name|restapi
operator|.
name|RestReadView
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

begin_class
DECL|class|Check
specifier|public
class|class
name|Check
implements|implements
name|RestReadView
argument_list|<
name|ChangeResource
argument_list|>
implements|,
name|RestModifyView
argument_list|<
name|ChangeResource
argument_list|,
name|FixInput
argument_list|>
block|{
DECL|field|json
specifier|private
specifier|final
name|ChangeJson
name|json
decl_stmt|;
annotation|@
name|Inject
DECL|method|Check (ChangeJson json)
name|Check
parameter_list|(
name|ChangeJson
name|json
parameter_list|)
block|{
name|this
operator|.
name|json
operator|=
name|json
expr_stmt|;
name|json
operator|.
name|addOption
argument_list|(
name|ListChangesOption
operator|.
name|CHECK
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|)
throws|throws
name|OrmException
block|{
return|return
name|GetChange
operator|.
name|cache
argument_list|(
name|json
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeResource rsrc, FixInput input)
specifier|public
name|Response
argument_list|<
name|ChangeInfo
argument_list|>
name|apply
parameter_list|(
name|ChangeResource
name|rsrc
parameter_list|,
name|FixInput
name|input
parameter_list|)
throws|throws
name|AuthException
throws|,
name|OrmException
block|{
name|ChangeControl
name|ctl
init|=
name|rsrc
operator|.
name|getControl
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|ctl
operator|.
name|isOwner
argument_list|()
operator|&&
operator|!
name|ctl
operator|.
name|getProjectControl
argument_list|()
operator|.
name|isOwner
argument_list|()
operator|&&
operator|!
name|ctl
operator|.
name|getCurrentUser
argument_list|()
operator|.
name|getCapabilities
argument_list|()
operator|.
name|canAdministrateServer
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|AuthException
argument_list|(
literal|"Not owner"
argument_list|)
throw|;
block|}
return|return
name|GetChange
operator|.
name|cache
argument_list|(
name|json
operator|.
name|fix
argument_list|(
name|input
argument_list|)
operator|.
name|format
argument_list|(
name|rsrc
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

