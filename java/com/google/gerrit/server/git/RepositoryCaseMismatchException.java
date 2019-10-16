begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2011 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.git
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
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
name|entities
operator|.
name|Project
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
name|errors
operator|.
name|RepositoryNotFoundException
import|;
end_import

begin_comment
comment|/**  * This exception is thrown if a project cannot be created because a project with the same name in a  * different case already exists. This can only happen if the OS has a case insensitive file system  * (e.g. Windows), because in this case the name for the git repository in the file system is  * already occupied by the existing project.  */
end_comment

begin_class
DECL|class|RepositoryCaseMismatchException
specifier|public
class|class
name|RepositoryCaseMismatchException
extends|extends
name|RepositoryNotFoundException
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
comment|/** @param projectName name of the project that cannot be created */
DECL|method|RepositoryCaseMismatchException (Project.NameKey projectName)
specifier|public
name|RepositoryCaseMismatchException
parameter_list|(
name|Project
operator|.
name|NameKey
name|projectName
parameter_list|)
block|{
name|super
argument_list|(
literal|"Name occupied in other case. Project "
operator|+
name|projectName
operator|.
name|get
argument_list|()
operator|+
literal|" cannot be created."
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

