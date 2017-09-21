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
DECL|package|com.google.gerrit.server.update
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|update
package|;
end_package

begin_comment
comment|/**  * Base interface for operations performed as part of a {@link BatchUpdate}.  *  *<p>Operations that implement this type only touch the repository; they cannot touch change  * storage, nor are they even associated with a change ID. To modify a change, implement {@link  * BatchUpdateOp} instead.  */
end_comment

begin_interface
DECL|interface|RepoOnlyOp
specifier|public
interface|interface
name|RepoOnlyOp
block|{
comment|/**    * Override this method to update the repo.    *    * @param ctx context    */
DECL|method|updateRepo (RepoContext ctx)
specifier|default
name|void
name|updateRepo
parameter_list|(
name|RepoContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{}
comment|/**    * Override this method to do something after the update e.g. send email or run hooks    *    * @param ctx context    */
comment|//TODO(dborowitz): Support async operations?
DECL|method|postUpdate (Context ctx)
specifier|default
name|void
name|postUpdate
parameter_list|(
name|Context
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{}
block|}
end_interface

end_unit

