begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2016 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.notedb.rebuild
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|notedb
operator|.
name|rebuild
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
name|PatchSet
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
name|notedb
operator|.
name|ChangeUpdate
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_class
DECL|class|CreateChangeEvent
class|class
name|CreateChangeEvent
extends|extends
name|Event
block|{
DECL|field|change
specifier|private
specifier|final
name|Change
name|change
decl_stmt|;
DECL|method|psId (Change change, Integer minPsNum)
specifier|private
specifier|static
name|PatchSet
operator|.
name|Id
name|psId
parameter_list|(
name|Change
name|change
parameter_list|,
name|Integer
name|minPsNum
parameter_list|)
block|{
name|int
name|n
decl_stmt|;
if|if
condition|(
name|minPsNum
operator|==
literal|null
condition|)
block|{
comment|// There were no patch sets for the change at all, so something is very
comment|// wrong. Bail and use 1 as the patch set.
name|n
operator|=
literal|1
expr_stmt|;
block|}
else|else
block|{
name|n
operator|=
name|minPsNum
expr_stmt|;
block|}
return|return
operator|new
name|PatchSet
operator|.
name|Id
argument_list|(
name|change
operator|.
name|getId
argument_list|()
argument_list|,
name|n
argument_list|)
return|;
block|}
DECL|method|CreateChangeEvent (Change change, Integer minPsNum)
name|CreateChangeEvent
parameter_list|(
name|Change
name|change
parameter_list|,
name|Integer
name|minPsNum
parameter_list|)
block|{
name|super
argument_list|(
name|psId
argument_list|(
name|change
argument_list|,
name|minPsNum
argument_list|)
argument_list|,
name|change
operator|.
name|getOwner
argument_list|()
argument_list|,
name|change
operator|.
name|getOwner
argument_list|()
argument_list|,
name|change
operator|.
name|getCreatedOn
argument_list|()
argument_list|,
name|change
operator|.
name|getCreatedOn
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|change
operator|=
name|change
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|uniquePerUpdate ()
name|boolean
name|uniquePerUpdate
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|apply (ChangeUpdate update)
name|void
name|apply
parameter_list|(
name|ChangeUpdate
name|update
parameter_list|)
throws|throws
name|IOException
throws|,
name|OrmException
block|{
name|checkUpdate
argument_list|(
name|update
argument_list|)
expr_stmt|;
name|ChangeRebuilderImpl
operator|.
name|createChange
argument_list|(
name|update
argument_list|,
name|change
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

