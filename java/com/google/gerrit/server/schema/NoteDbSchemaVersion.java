begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.schema
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|schema
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
name|server
operator|.
name|config
operator|.
name|AllProjectsName
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
name|config
operator|.
name|AllUsersName
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

begin_comment
comment|/**  * Schema upgrade implementation.  *  *<p>Implementations must have a single non-private constructor with no arguments (e.g. the default  * constructor).  */
end_comment

begin_interface
DECL|interface|NoteDbSchemaVersion
interface|interface
name|NoteDbSchemaVersion
block|{
annotation|@
name|Singleton
DECL|class|Arguments
class|class
name|Arguments
block|{
DECL|field|repoManager
specifier|final
name|GitRepositoryManager
name|repoManager
decl_stmt|;
DECL|field|allProjects
specifier|final
name|AllProjectsName
name|allProjects
decl_stmt|;
DECL|field|allUsers
specifier|final
name|AllUsersName
name|allUsers
decl_stmt|;
annotation|@
name|Inject
DECL|method|Arguments ( GitRepositoryManager repoManager, AllProjectsName allProjects, AllUsersName allUsers)
name|Arguments
parameter_list|(
name|GitRepositoryManager
name|repoManager
parameter_list|,
name|AllProjectsName
name|allProjects
parameter_list|,
name|AllUsersName
name|allUsers
parameter_list|)
block|{
name|this
operator|.
name|repoManager
operator|=
name|repoManager
expr_stmt|;
name|this
operator|.
name|allProjects
operator|=
name|allProjects
expr_stmt|;
name|this
operator|.
name|allUsers
operator|=
name|allUsers
expr_stmt|;
block|}
block|}
DECL|method|upgrade (Arguments args, UpdateUI ui)
name|void
name|upgrade
parameter_list|(
name|Arguments
name|args
parameter_list|,
name|UpdateUI
name|ui
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

