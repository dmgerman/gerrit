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
name|common
operator|.
name|RevisionInfo
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
name|RevisionEvent
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
comment|/** Base class for all revision events. */
end_comment

begin_class
DECL|class|AbstractRevisionEvent
specifier|public
specifier|abstract
class|class
name|AbstractRevisionEvent
extends|extends
name|AbstractChangeEvent
implements|implements
name|RevisionEvent
block|{
DECL|field|revisionInfo
specifier|private
specifier|final
name|RevisionInfo
name|revisionInfo
decl_stmt|;
DECL|method|AbstractRevisionEvent ( ChangeInfo change, RevisionInfo revision, AccountInfo who, Timestamp when, NotifyHandling notify)
specifier|protected
name|AbstractRevisionEvent
parameter_list|(
name|ChangeInfo
name|change
parameter_list|,
name|RevisionInfo
name|revision
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
name|super
argument_list|(
name|change
argument_list|,
name|who
argument_list|,
name|when
argument_list|,
name|notify
argument_list|)
expr_stmt|;
name|revisionInfo
operator|=
name|revision
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRevision ()
specifier|public
name|RevisionInfo
name|getRevision
parameter_list|()
block|{
return|return
name|revisionInfo
return|;
block|}
block|}
end_class

end_unit

