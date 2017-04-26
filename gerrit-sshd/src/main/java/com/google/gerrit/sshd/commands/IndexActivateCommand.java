begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2015 The Android Open Source Project
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
name|lucene
operator|.
name|LuceneVersionManager
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
name|index
operator|.
name|ReindexerAlreadyRunningException
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
name|Argument
import|;
end_import

begin_class
annotation|@
name|RequiresCapability
argument_list|(
name|GlobalCapability
operator|.
name|ADMINISTRATE_SERVER
argument_list|)
annotation|@
name|CommandMetaData
argument_list|(
name|name
operator|=
literal|"activate"
argument_list|,
name|description
operator|=
literal|"Activate the latest index version available"
argument_list|)
DECL|class|IndexActivateCommand
specifier|public
class|class
name|IndexActivateCommand
extends|extends
name|SshCommand
block|{
annotation|@
name|Argument
argument_list|(
name|index
operator|=
literal|0
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|metaVar
operator|=
literal|"INDEX"
argument_list|,
name|usage
operator|=
literal|"index name to activate"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|luceneVersionManager
annotation|@
name|Inject
specifier|private
name|LuceneVersionManager
name|luceneVersionManager
decl_stmt|;
annotation|@
name|Override
DECL|method|run ()
specifier|protected
name|void
name|run
parameter_list|()
throws|throws
name|UnloggedFailure
block|{
try|try
block|{
if|if
condition|(
name|luceneVersionManager
operator|.
name|activateLatestIndex
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|stdout
operator|.
name|println
argument_list|(
literal|"Activated latest index version"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stdout
operator|.
name|println
argument_list|(
literal|"Not activating index, already using latest version"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ReindexerAlreadyRunningException
name|e
parameter_list|)
block|{
throw|throw
name|die
argument_list|(
literal|"Failed to activate latest index: "
operator|+
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

