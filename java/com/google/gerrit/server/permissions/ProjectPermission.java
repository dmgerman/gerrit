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
DECL|package|com.google.gerrit.server.permissions
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|permissions
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
name|access
operator|.
name|GerritPermission
import|;
end_import

begin_enum
DECL|enum|ProjectPermission
specifier|public
enum|enum
name|ProjectPermission
implements|implements
name|GerritPermission
block|{
comment|/**    * Can access at least one reference or change within the repository.    *    *<p>Checking this permission instead of {@link #READ} may require filtering to hide specific    * references or changes, which can be expensive.    */
DECL|enumConstant|ACCESS
name|ACCESS
block|,
comment|/**    * Can read all references in the repository.    *    *<p>This is a stronger form of {@link #ACCESS} where no filtering is required.    */
DECL|enumConstant|READ
name|READ
block|,
comment|/**    * Can create at least one reference in the project.    *    *<p>This project level permission only validates the user may create some type of reference    * within the project. The exact reference name must be checked at creation:    *    *<pre>permissionBackend    *    .user(user)    *    .project(proj)    *    .ref(ref)    *    .check(RefPermission.CREATE);    *</pre>    */
DECL|enumConstant|CREATE_REF
name|CREATE_REF
block|,
comment|/**    * Can create at least one change in the project.    *    *<p>This project level permission only validates the user may create a change for some branch    * within the project. The exact reference name must be checked at creation:    *    *<pre>permissionBackend    *    .user(user)    *    .project(proj)    *    .ref(ref)    *    .check(RefPermission.CREATE_CHANGE);    *</pre>    */
DECL|enumConstant|CREATE_CHANGE
name|CREATE_CHANGE
block|,
comment|/** Can run receive pack. */
DECL|enumConstant|RUN_RECEIVE_PACK
name|RUN_RECEIVE_PACK
block|,
comment|/** Can run upload pack. */
DECL|enumConstant|RUN_UPLOAD_PACK
name|RUN_UPLOAD_PACK
block|,
comment|/** Allow read access to refs/meta/config. */
DECL|enumConstant|READ_CONFIG
name|READ_CONFIG
block|,
comment|/** Allow write access to refs/meta/config. */
DECL|enumConstant|WRITE_CONFIG
name|WRITE_CONFIG
block|,
comment|/** Allow banning commits from Gerrit preventing pushes of these commits. */
DECL|enumConstant|BAN_COMMIT
name|BAN_COMMIT
block|,
comment|/** Allow accessing the project's reflog. */
DECL|enumConstant|READ_REFLOG
name|READ_REFLOG
block|,
comment|/** Can push to at least one reference within the repository. */
DECL|enumConstant|PUSH_AT_LEAST_ONE_REF
name|PUSH_AT_LEAST_ONE_REF
block|;
annotation|@
name|Deprecated
annotation|@
name|Override
DECL|method|permissionName ()
specifier|public
name|String
name|permissionName
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_enum

end_unit

