begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2012 The Android Open Source Project
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
DECL|package|com.google.gerrit.server
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
package|;
end_package

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Objects
operator|.
name|requireNonNull
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|auto
operator|.
name|value
operator|.
name|AutoValue
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableListMultimap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ListMultimap
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
name|util
operator|.
name|time
operator|.
name|TimeUtil
import|;
end_import

begin_class
DECL|class|AuditEvent
specifier|public
class|class
name|AuditEvent
block|{
DECL|field|UNKNOWN_SESSION_ID
specifier|public
specifier|static
specifier|final
name|String
name|UNKNOWN_SESSION_ID
init|=
literal|"000000000000000000000000000"
decl_stmt|;
DECL|field|EMPTY_PARAMS
specifier|protected
specifier|static
specifier|final
name|ImmutableListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|EMPTY_PARAMS
init|=
name|ImmutableListMultimap
operator|.
name|of
argument_list|()
decl_stmt|;
DECL|field|sessionId
specifier|public
specifier|final
name|String
name|sessionId
decl_stmt|;
DECL|field|who
specifier|public
specifier|final
name|CurrentUser
name|who
decl_stmt|;
DECL|field|when
specifier|public
specifier|final
name|long
name|when
decl_stmt|;
DECL|field|what
specifier|public
specifier|final
name|String
name|what
decl_stmt|;
DECL|field|params
specifier|public
specifier|final
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|params
decl_stmt|;
DECL|field|result
specifier|public
specifier|final
name|Object
name|result
decl_stmt|;
DECL|field|timeAtStart
specifier|public
specifier|final
name|long
name|timeAtStart
decl_stmt|;
DECL|field|elapsed
specifier|public
specifier|final
name|long
name|elapsed
decl_stmt|;
DECL|field|uuid
specifier|public
specifier|final
name|UUID
name|uuid
decl_stmt|;
annotation|@
name|AutoValue
DECL|class|UUID
specifier|public
specifier|abstract
specifier|static
class|class
name|UUID
block|{
DECL|method|create ()
specifier|private
specifier|static
name|UUID
name|create
parameter_list|()
block|{
return|return
operator|new
name|AutoValue_AuditEvent_UUID
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"audit:%s"
argument_list|,
name|java
operator|.
name|util
operator|.
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|uuid ()
specifier|public
specifier|abstract
name|String
name|uuid
parameter_list|()
function_decl|;
block|}
comment|/**    * Creates a new audit event with results    *    * @param sessionId session id the event belongs to    * @param who principal that has generated the event    * @param what object of the event    * @param when time-stamp of when the event started    * @param params parameters of the event    * @param result result of the event    */
DECL|method|AuditEvent ( String sessionId, CurrentUser who, String what, long when, ListMultimap<String, ?> params, Object result)
specifier|public
name|AuditEvent
parameter_list|(
name|String
name|sessionId
parameter_list|,
name|CurrentUser
name|who
parameter_list|,
name|String
name|what
parameter_list|,
name|long
name|when
parameter_list|,
name|ListMultimap
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|params
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
name|requireNonNull
argument_list|(
name|what
argument_list|,
literal|"what is a mandatory not null param !"
argument_list|)
expr_stmt|;
name|this
operator|.
name|sessionId
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|sessionId
argument_list|,
name|UNKNOWN_SESSION_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|who
operator|=
name|who
expr_stmt|;
name|this
operator|.
name|what
operator|=
name|what
expr_stmt|;
name|this
operator|.
name|when
operator|=
name|when
expr_stmt|;
name|this
operator|.
name|timeAtStart
operator|=
name|this
operator|.
name|when
expr_stmt|;
name|this
operator|.
name|params
operator|=
name|MoreObjects
operator|.
name|firstNonNull
argument_list|(
name|params
argument_list|,
name|EMPTY_PARAMS
argument_list|)
expr_stmt|;
name|this
operator|.
name|uuid
operator|=
name|UUID
operator|.
name|create
argument_list|()
expr_stmt|;
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
name|this
operator|.
name|elapsed
operator|=
name|TimeUtil
operator|.
name|nowMs
argument_list|()
operator|-
name|timeAtStart
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|uuid
operator|.
name|hashCode
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|obj
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|obj
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|getClass
argument_list|()
operator|!=
name|obj
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AuditEvent
name|other
init|=
operator|(
name|AuditEvent
operator|)
name|obj
decl_stmt|;
return|return
name|this
operator|.
name|uuid
operator|.
name|equals
argument_list|(
name|other
operator|.
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"AuditEvent UUID:%s, SID:%s, TS:%d, who:%s, what:%s"
argument_list|,
name|uuid
operator|.
name|uuid
argument_list|()
argument_list|,
name|sessionId
argument_list|,
name|when
argument_list|,
name|who
argument_list|,
name|what
argument_list|)
return|;
block|}
block|}
end_class

end_unit

