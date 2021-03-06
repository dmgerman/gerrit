begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2008 The Android Open Source Project
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
DECL|package|com.google.gerrit.entities
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|entities
package|;
end_package

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
name|gerrit
operator|.
name|common
operator|.
name|Nullable
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/** A message attached to a {@link Change}. */
end_comment

begin_class
DECL|class|ChangeMessage
specifier|public
specifier|final
class|class
name|ChangeMessage
block|{
DECL|method|key (Change.Id changeId, String uuid)
specifier|public
specifier|static
name|Key
name|key
parameter_list|(
name|Change
operator|.
name|Id
name|changeId
parameter_list|,
name|String
name|uuid
parameter_list|)
block|{
return|return
operator|new
name|AutoValue_ChangeMessage_Key
argument_list|(
name|changeId
argument_list|,
name|uuid
argument_list|)
return|;
block|}
annotation|@
name|AutoValue
DECL|class|Key
specifier|public
specifier|abstract
specifier|static
class|class
name|Key
block|{
DECL|method|changeId ()
specifier|public
specifier|abstract
name|Change
operator|.
name|Id
name|changeId
parameter_list|()
function_decl|;
DECL|method|uuid ()
specifier|public
specifier|abstract
name|String
name|uuid
parameter_list|()
function_decl|;
block|}
DECL|field|key
specifier|protected
name|Key
name|key
decl_stmt|;
comment|/** Who wrote this comment; null if it was written by the Gerrit system. */
DECL|field|author
annotation|@
name|Nullable
specifier|protected
name|Account
operator|.
name|Id
name|author
decl_stmt|;
comment|/** When this comment was drafted. */
DECL|field|writtenOn
specifier|protected
name|Timestamp
name|writtenOn
decl_stmt|;
comment|/** The text left by the user. */
DECL|field|message
annotation|@
name|Nullable
specifier|protected
name|String
name|message
decl_stmt|;
comment|/** Which patchset (if any) was this message generated from? */
DECL|field|patchset
annotation|@
name|Nullable
specifier|protected
name|PatchSet
operator|.
name|Id
name|patchset
decl_stmt|;
comment|/** Tag associated with change message */
DECL|field|tag
annotation|@
name|Nullable
specifier|protected
name|String
name|tag
decl_stmt|;
comment|/** Real user that added this message on behalf of the user recorded in {@link #author}. */
DECL|field|realAuthor
annotation|@
name|Nullable
specifier|protected
name|Account
operator|.
name|Id
name|realAuthor
decl_stmt|;
DECL|method|ChangeMessage ()
specifier|protected
name|ChangeMessage
parameter_list|()
block|{}
DECL|method|ChangeMessage (final ChangeMessage.Key k, Account.Id a, Timestamp wo, PatchSet.Id psid)
specifier|public
name|ChangeMessage
parameter_list|(
specifier|final
name|ChangeMessage
operator|.
name|Key
name|k
parameter_list|,
name|Account
operator|.
name|Id
name|a
parameter_list|,
name|Timestamp
name|wo
parameter_list|,
name|PatchSet
operator|.
name|Id
name|psid
parameter_list|)
block|{
name|key
operator|=
name|k
expr_stmt|;
name|author
operator|=
name|a
expr_stmt|;
name|writtenOn
operator|=
name|wo
expr_stmt|;
name|patchset
operator|=
name|psid
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|ChangeMessage
operator|.
name|Key
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/** If null, the message was written 'by the Gerrit system'. */
DECL|method|getAuthor ()
specifier|public
name|Account
operator|.
name|Id
name|getAuthor
parameter_list|()
block|{
return|return
name|author
return|;
block|}
DECL|method|setAuthor (Account.Id accountId)
specifier|public
name|void
name|setAuthor
parameter_list|(
name|Account
operator|.
name|Id
name|accountId
parameter_list|)
block|{
if|if
condition|(
name|author
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot modify author once assigned"
argument_list|)
throw|;
block|}
name|author
operator|=
name|accountId
expr_stmt|;
block|}
DECL|method|getRealAuthor ()
specifier|public
name|Account
operator|.
name|Id
name|getRealAuthor
parameter_list|()
block|{
return|return
name|realAuthor
operator|!=
literal|null
condition|?
name|realAuthor
else|:
name|getAuthor
argument_list|()
return|;
block|}
DECL|method|setRealAuthor (Account.Id id)
specifier|public
name|void
name|setRealAuthor
parameter_list|(
name|Account
operator|.
name|Id
name|id
parameter_list|)
block|{
comment|// Use null for same real author, as before the column was added.
name|realAuthor
operator|=
name|Objects
operator|.
name|equals
argument_list|(
name|getAuthor
argument_list|()
argument_list|,
name|id
argument_list|)
condition|?
literal|null
else|:
name|id
expr_stmt|;
block|}
DECL|method|getWrittenOn ()
specifier|public
name|Timestamp
name|getWrittenOn
parameter_list|()
block|{
return|return
name|writtenOn
return|;
block|}
DECL|method|setWrittenOn (Timestamp ts)
specifier|public
name|void
name|setWrittenOn
parameter_list|(
name|Timestamp
name|ts
parameter_list|)
block|{
name|writtenOn
operator|=
name|ts
expr_stmt|;
block|}
DECL|method|getMessage ()
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|message
return|;
block|}
DECL|method|setMessage (String s)
specifier|public
name|void
name|setMessage
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|message
operator|=
name|s
expr_stmt|;
block|}
DECL|method|getTag ()
specifier|public
name|String
name|getTag
parameter_list|()
block|{
return|return
name|tag
return|;
block|}
DECL|method|setTag (String tag)
specifier|public
name|void
name|setTag
parameter_list|(
name|String
name|tag
parameter_list|)
block|{
name|this
operator|.
name|tag
operator|=
name|tag
expr_stmt|;
block|}
DECL|method|getPatchSetId ()
specifier|public
name|PatchSet
operator|.
name|Id
name|getPatchSetId
parameter_list|()
block|{
return|return
name|patchset
return|;
block|}
DECL|method|setPatchSetId (PatchSet.Id id)
specifier|public
name|void
name|setPatchSetId
parameter_list|(
name|PatchSet
operator|.
name|Id
name|id
parameter_list|)
block|{
name|patchset
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|ChangeMessage
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|ChangeMessage
name|m
init|=
operator|(
name|ChangeMessage
operator|)
name|o
decl_stmt|;
return|return
name|Objects
operator|.
name|equals
argument_list|(
name|key
argument_list|,
name|m
operator|.
name|key
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|author
argument_list|,
name|m
operator|.
name|author
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|writtenOn
argument_list|,
name|m
operator|.
name|writtenOn
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|message
argument_list|,
name|m
operator|.
name|message
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|patchset
argument_list|,
name|m
operator|.
name|patchset
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|tag
argument_list|,
name|m
operator|.
name|tag
argument_list|)
operator|&&
name|Objects
operator|.
name|equals
argument_list|(
name|realAuthor
argument_list|,
name|m
operator|.
name|realAuthor
argument_list|)
return|;
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
name|Objects
operator|.
name|hash
argument_list|(
name|key
argument_list|,
name|author
argument_list|,
name|writtenOn
argument_list|,
name|message
argument_list|,
name|patchset
argument_list|,
name|tag
argument_list|,
name|realAuthor
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
literal|"ChangeMessage{"
operator|+
literal|"key="
operator|+
name|key
operator|+
literal|", author="
operator|+
name|author
operator|+
literal|", realAuthor="
operator|+
name|realAuthor
operator|+
literal|", writtenOn="
operator|+
name|writtenOn
operator|+
literal|", patchset="
operator|+
name|patchset
operator|+
literal|", tag="
operator|+
name|tag
operator|+
literal|", message=["
operator|+
name|message
operator|+
literal|"]}"
return|;
block|}
block|}
end_class

end_unit

