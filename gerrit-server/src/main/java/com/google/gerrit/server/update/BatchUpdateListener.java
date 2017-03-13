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
comment|/**  * Interface for listening during batch update execution.  *  *<p>When used during execution of multiple batch updates, the {@code after*} methods are called  * after that phase has been completed for<em>all</em> updates.  */
end_comment

begin_interface
DECL|interface|BatchUpdateListener
specifier|public
interface|interface
name|BatchUpdateListener
block|{
DECL|field|NONE
specifier|public
specifier|static
specifier|final
name|BatchUpdateListener
name|NONE
init|=
operator|new
name|BatchUpdateListener
argument_list|()
block|{}
decl_stmt|;
comment|/** Called after updating all repositories and flushing objects but before updating any refs. */
DECL|method|afterUpdateRepos ()
specifier|default
name|void
name|afterUpdateRepos
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/** Called after updating all refs. */
DECL|method|afterUpdateRefs ()
specifier|default
name|void
name|afterUpdateRefs
parameter_list|()
throws|throws
name|Exception
block|{}
comment|/** Called after updating all changes. */
DECL|method|afterUpdateChanges ()
specifier|default
name|void
name|afterUpdateChanges
parameter_list|()
throws|throws
name|Exception
block|{}
block|}
end_interface

end_unit

