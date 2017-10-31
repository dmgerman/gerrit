begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2009 The Android Open Source Project
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
DECL|package|com.google.gerrit.sshd.commands
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|sshd
operator|.
name|commands
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
name|GlobalCapability
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
name|annotations
operator|.
name|RequiresCapability
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
name|server
operator|.
name|IdentifiedUser
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
name|GlobalPermission
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
name|PermissionBackend
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
name|sshd
operator|.
name|AdminHighPriorityCommand
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
name|sshd
operator|.
name|CommandMetaData
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
name|sshd
operator|.
name|SshCommand
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
name|org
operator|.
name|kohsuke
operator|.
name|args4j
operator|.
name|Option
import|;
end_import

begin_comment
comment|/** Opens a query processor. */
end_comment

begin_class
annotation|@
name|AdminHighPriorityCommand
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ACCESS_DATABASE
argument_list|)
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"gsql"
argument_list|,
name|description
operator|=
literal|"Administrative interface to active database"
argument_list|)
DECL|class|AdminQueryShell
specifier|final
class|class
name|AdminQueryShell
extends|extends
name|SshCommand
block|{
DECL|field|permissionBackend
annotation|@
name|Inject
specifier|private
name|PermissionBackend
name|permissionBackend
decl_stmt|;
DECL|field|factory
annotation|@
name|Inject
specifier|private
name|QueryShell
operator|.
name|Factory
name|factory
decl_stmt|;
DECL|field|currentUser
annotation|@
name|Inject
specifier|private
name|IdentifiedUser
name|currentUser
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"--format"
argument_list|,
name|usage
operator|=
literal|"Set output format"
argument_list|)
DECL|field|format
specifier|private
name|QueryShell
operator|.
name|OutputFormat
name|format
init|=
name|QueryShell
operator|.
name|OutputFormat
operator|.
name|PRETTY
decl_stmt|;
annotation|@
name|Option
argument_list|(
name|name
operator|=
literal|"-c"
argument_list|,
name|metaVar
operator|=
literal|"SQL QUERY"
argument_list|,
name|usage
operator|=
literal|"Query to execute"
argument_list|)
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|Failure
block|{
try|try
block|{
name|permissionBackend
operator|.
name|user
argument_list|(
name|currentUser
argument_list|)
operator|.
name|check
argument_list|(
name|GlobalPermission
operator|.
name|ACCESS_DATABASE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|AuthException
name|err
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
name|err
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PermissionBackendException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Failure
argument_list|(
literal|1
argument_list|,
literal|"unavailable"
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|QueryShell
name|shell
init|=
name|factory
operator|.
name|create
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|shell
operator|.
name|setOutputFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
if|if
condition|(
name|query
operator|!=
literal|null
condition|)
block|{
name|shell
operator|.
name|execute
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|shell
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

