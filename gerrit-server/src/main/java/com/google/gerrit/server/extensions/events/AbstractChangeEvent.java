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
DECL|package|com.google.gerrit.server.extensions.events
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|extensions
operator|.
name|events
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
name|events
operator|.
name|ChangeEvent
import|;
end_import

begin_class
DECL|class|AbstractChangeEvent
specifier|public
specifier|abstract
class|class
name|AbstractChangeEvent
implements|implements
name|ChangeEvent
block|{
DECL|field|changeInfo
specifier|private
specifier|final
name|ChangeInfo
name|changeInfo
decl_stmt|;
DECL|method|AbstractChangeEvent (ChangeInfo change)
specifier|protected
name|AbstractChangeEvent
parameter_list|(
name|ChangeInfo
name|change
parameter_list|)
block|{
name|this
operator|.
name|changeInfo
operator|=
name|change
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getChange ()
specifier|public
name|ChangeInfo
name|getChange
parameter_list|()
block|{
return|return
name|changeInfo
return|;
block|}
block|}
end_class

end_unit

