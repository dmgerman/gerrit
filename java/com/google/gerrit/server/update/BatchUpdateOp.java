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
comment|/**  * Interface for {@link BatchUpdate} operations that touch a change.  *  *<p>Each operation has {@link #updateChange(ChangeContext)} called once the change is read in a  * transaction. Ops are associated with updates via {@link  * BatchUpdate#addOp(com.google.gerrit.entities.Change.Id, BatchUpdateOp)}.  *  *<p>Usually, a single {@code BatchUpdateOp} instance is only associated with a single change, i.e.  * {@code addOp} is only called once with that instance. Additionally, each method in {@code  * BatchUpdateOp} is called at most once per {@link BatchUpdate} execution.  *  *<p>Taken together, these two properties mean an instance may communicate between phases by  * storing data in private fields, and a single instance must not be reused.  */
end_comment

begin_interface
DECL|interface|BatchUpdateOp
specifier|public
interface|interface
name|BatchUpdateOp
extends|extends
name|RepoOnlyOp
block|{
comment|/**    * Override this method to modify a change.    *    * @param ctx context    * @return whether anything was changed that might require a write to the metadata storage.    */
DECL|method|updateChange (ChangeContext ctx)
specifier|default
name|boolean
name|updateChange
parameter_list|(
name|ChangeContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|false
return|;
block|}
block|}
end_interface

end_unit

