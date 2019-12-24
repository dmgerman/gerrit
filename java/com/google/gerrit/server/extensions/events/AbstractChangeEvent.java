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
name|api
operator|.
name|changes
operator|.
name|NotifyHandling
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
name|common
operator|.
name|AccountInfo
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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
import|;
end_import

begin_comment
comment|/** Base class for all change events. */
end_comment

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
DECL|field|who
specifier|private
specifier|final
name|AccountInfo
name|who
decl_stmt|;
DECL|field|when
specifier|private
specifier|final
name|Timestamp
name|when
decl_stmt|;
DECL|field|notify
specifier|private
specifier|final
name|NotifyHandling
name|notify
decl_stmt|;
DECL|method|AbstractChangeEvent ( ChangeInfo change, AccountInfo who, Timestamp when, NotifyHandling notify)
specifier|protected
name|AbstractChangeEvent
parameter_list|(
name|ChangeInfo
name|change
parameter_list|,
name|AccountInfo
name|who
parameter_list|,
name|Timestamp
name|when
parameter_list|,
name|NotifyHandling
name|notify
parameter_list|)
block|{
name|this
operator|.
name|changeInfo
operator|=
name|change
expr_stmt|;
name|this
operator|.
name|who
operator|=
name|who
expr_stmt|;
name|this
operator|.
name|when
operator|=
name|when
expr_stmt|;
name|this
operator|.
name|notify
operator|=
name|notify
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
annotation|@
name|Override
DECL|method|getWho ()
specifier|public
name|AccountInfo
name|getWho
parameter_list|()
block|{
return|return
name|who
return|;
block|}
annotation|@
name|Override
DECL|method|getWhen ()
specifier|public
name|Timestamp
name|getWhen
parameter_list|()
block|{
return|return
name|when
return|;
block|}
annotation|@
name|Override
DECL|method|getNotify ()
specifier|public
name|NotifyHandling
name|getNotify
parameter_list|()
block|{
return|return
name|notify
return|;
block|}
block|}
end_class

end_unit

