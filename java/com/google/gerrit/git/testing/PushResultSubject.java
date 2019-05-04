begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2018 The Android Open Source Project
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
DECL|package|com.google.gerrit.git.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|testing
package|;
end_package

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Preconditions
operator|.
name|checkArgument
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Fact
operator|.
name|fact
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
operator|.
name|assertAbout
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|git
operator|.
name|testing
operator|.
name|PushResultSubject
operator|.
name|RemoteRefUpdateSubject
operator|.
name|refs
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
name|annotations
operator|.
name|VisibleForTesting
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
name|Splitter
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
name|Strings
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
name|Throwables
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
name|ImmutableMap
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
name|Maps
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
name|truth
operator|.
name|FailureMetadata
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
name|truth
operator|.
name|StreamSubject
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
name|truth
operator|.
name|Subject
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
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|PushResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|transport
operator|.
name|RemoteRefUpdate
import|;
end_import

begin_class
DECL|class|PushResultSubject
specifier|public
class|class
name|PushResultSubject
extends|extends
name|Subject
argument_list|<
name|PushResultSubject
argument_list|,
name|PushResult
argument_list|>
block|{
DECL|method|assertThat (PushResult actual)
specifier|public
specifier|static
name|PushResultSubject
name|assertThat
parameter_list|(
name|PushResult
name|actual
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|PushResultSubject
operator|::
operator|new
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|)
return|;
block|}
DECL|method|PushResultSubject (FailureMetadata metadata, PushResult actual)
specifier|private
name|PushResultSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|PushResult
name|actual
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|hasNoMessages ()
specifier|public
name|void
name|hasNoMessages
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"hasNoMessages()"
argument_list|)
operator|.
name|that
argument_list|(
name|Strings
operator|.
name|nullToEmpty
argument_list|(
name|getTrimmedMessages
argument_list|()
argument_list|)
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|""
argument_list|)
expr_stmt|;
block|}
DECL|method|hasMessages (String... expectedLines)
specifier|public
name|void
name|hasMessages
parameter_list|(
name|String
modifier|...
name|expectedLines
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|expectedLines
operator|.
name|length
operator|>
literal|0
argument_list|,
literal|"use hasNoMessages()"
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getTrimmedMessages()"
argument_list|)
operator|.
name|that
argument_list|(
name|getTrimmedMessages
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|String
operator|.
name|join
argument_list|(
literal|"\n"
argument_list|,
name|expectedLines
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|containsMessages (String... expectedLines)
specifier|public
name|void
name|containsMessages
parameter_list|(
name|String
modifier|...
name|expectedLines
parameter_list|)
block|{
name|checkArgument
argument_list|(
name|expectedLines
operator|.
name|length
operator|>
literal|0
argument_list|,
literal|"use hasNoMessages()"
argument_list|)
expr_stmt|;
name|isNotNull
argument_list|()
expr_stmt|;
name|Iterable
argument_list|<
name|String
argument_list|>
name|got
init|=
name|Splitter
operator|.
name|on
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|split
argument_list|(
name|getTrimmedMessages
argument_list|()
argument_list|)
decl_stmt|;
name|check
argument_list|(
literal|"getTrimmedMessages()"
argument_list|)
operator|.
name|that
argument_list|(
name|got
argument_list|)
operator|.
name|containsAtLeastElementsIn
argument_list|(
name|expectedLines
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
DECL|method|getTrimmedMessages ()
specifier|private
name|String
name|getTrimmedMessages
parameter_list|()
block|{
return|return
name|trimMessages
argument_list|(
name|actual
argument_list|()
operator|.
name|getMessages
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
annotation|@
name|Nullable
DECL|method|trimMessages (@ullable String msg)
specifier|static
name|String
name|trimMessages
parameter_list|(
annotation|@
name|Nullable
name|String
name|msg
parameter_list|)
block|{
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|idx
init|=
name|msg
operator|.
name|indexOf
argument_list|(
literal|"Processing changes:"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>=
literal|0
condition|)
block|{
name|msg
operator|=
name|msg
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
return|return
name|msg
operator|.
name|trim
argument_list|()
return|;
block|}
DECL|method|hasProcessed (ImmutableMap<String, Integer> expected)
specifier|public
name|void
name|hasProcessed
parameter_list|(
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|expected
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|actual
decl_stmt|;
name|String
name|messages
init|=
name|actual
argument_list|()
operator|.
name|getMessages
argument_list|()
decl_stmt|;
try|try
block|{
name|actual
operator|=
name|parseProcessed
argument_list|(
name|messages
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|failWithActual
argument_list|(
name|fact
argument_list|(
literal|"failed to parse \"Processing changes\" line from messages, reason:"
argument_list|,
name|Throwables
operator|.
name|getStackTraceAsString
argument_list|(
name|e
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
name|check
argument_list|(
literal|"processedCommands()"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|)
operator|.
name|containsExactlyEntriesIn
argument_list|(
name|expected
argument_list|)
operator|.
name|inOrder
argument_list|()
expr_stmt|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|parseProcessed (@ullable String messages)
specifier|static
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|parseProcessed
parameter_list|(
annotation|@
name|Nullable
name|String
name|messages
parameter_list|)
block|{
if|if
condition|(
name|messages
operator|==
literal|null
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
name|String
name|toSplit
init|=
name|messages
operator|.
name|trim
argument_list|()
decl_stmt|;
name|String
name|prefix
init|=
literal|"Processing changes: "
decl_stmt|;
name|int
name|idx
init|=
name|toSplit
operator|.
name|lastIndexOf
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
name|toSplit
operator|=
name|toSplit
operator|.
name|substring
argument_list|(
name|idx
operator|+
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|toSplit
operator|.
name|equals
argument_list|(
literal|"done"
argument_list|)
condition|)
block|{
return|return
name|ImmutableMap
operator|.
name|of
argument_list|()
return|;
block|}
name|String
name|done
init|=
literal|", done"
decl_stmt|;
if|if
condition|(
name|toSplit
operator|.
name|endsWith
argument_list|(
name|done
argument_list|)
condition|)
block|{
name|toSplit
operator|=
name|toSplit
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|toSplit
operator|.
name|length
argument_list|()
operator|-
name|done
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ImmutableMap
operator|.
name|copyOf
argument_list|(
name|Maps
operator|.
name|transformValues
argument_list|(
name|Splitter
operator|.
name|on
argument_list|(
literal|','
argument_list|)
operator|.
name|trimResults
argument_list|()
operator|.
name|withKeyValueSeparator
argument_list|(
literal|':'
argument_list|)
operator|.
name|split
argument_list|(
name|toSplit
argument_list|)
argument_list|,
comment|// trimResults() doesn't trim values in the map.
name|v
lambda|->
name|Integer
operator|.
name|parseInt
argument_list|(
name|v
operator|.
name|trim
argument_list|()
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|ref (String refName)
specifier|public
name|RemoteRefUpdateSubject
name|ref
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
return|return
name|check
argument_list|(
literal|"getRemoteUpdate(%s)"
argument_list|,
name|refName
argument_list|)
operator|.
name|about
argument_list|(
name|refs
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|getRemoteUpdate
argument_list|(
name|refName
argument_list|)
argument_list|)
return|;
block|}
DECL|method|onlyRef (String refName)
specifier|public
name|RemoteRefUpdateSubject
name|onlyRef
parameter_list|(
name|String
name|refName
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"setOfRefs()"
argument_list|)
operator|.
name|about
argument_list|(
name|StreamSubject
operator|.
name|streams
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|getRemoteUpdates
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|RemoteRefUpdate
operator|::
name|getRemoteName
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
name|refName
argument_list|)
expr_stmt|;
return|return
name|ref
argument_list|(
name|refName
argument_list|)
return|;
block|}
DECL|class|RemoteRefUpdateSubject
specifier|public
specifier|static
class|class
name|RemoteRefUpdateSubject
extends|extends
name|Subject
argument_list|<
name|RemoteRefUpdateSubject
argument_list|,
name|RemoteRefUpdate
argument_list|>
block|{
DECL|method|RemoteRefUpdateSubject (FailureMetadata metadata, RemoteRefUpdate actual)
specifier|private
name|RemoteRefUpdateSubject
parameter_list|(
name|FailureMetadata
name|metadata
parameter_list|,
name|RemoteRefUpdate
name|actual
parameter_list|)
block|{
name|super
argument_list|(
name|metadata
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
DECL|method|refs ()
specifier|static
name|Factory
argument_list|<
name|RemoteRefUpdateSubject
argument_list|,
name|RemoteRefUpdate
argument_list|>
name|refs
parameter_list|()
block|{
return|return
name|RemoteRefUpdateSubject
operator|::
operator|new
return|;
block|}
DECL|method|hasStatus (RemoteRefUpdate.Status status)
specifier|public
name|void
name|hasStatus
parameter_list|(
name|RemoteRefUpdate
operator|.
name|Status
name|status
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|RemoteRefUpdate
name|u
init|=
name|actual
argument_list|()
decl_stmt|;
name|check
argument_list|(
literal|"getStatus()"
argument_list|)
operator|.
name|withMessage
argument_list|(
literal|"status message: %s"
argument_list|,
name|u
operator|.
name|getMessage
argument_list|()
operator|!=
literal|null
condition|?
literal|": "
operator|+
name|u
operator|.
name|getMessage
argument_list|()
else|:
literal|"<emtpy>"
argument_list|)
operator|.
name|that
argument_list|(
name|u
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|status
argument_list|)
expr_stmt|;
block|}
DECL|method|hasNoMessage ()
specifier|public
name|void
name|hasNoMessage
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getMessage()"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
DECL|method|hasMessage (String expected)
specifier|public
name|void
name|hasMessage
parameter_list|(
name|String
name|expected
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|check
argument_list|(
literal|"getMessage()"
argument_list|)
operator|.
name|that
argument_list|(
name|actual
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|expected
argument_list|)
expr_stmt|;
block|}
DECL|method|isOk ()
specifier|public
name|void
name|isOk
parameter_list|()
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|hasStatus
argument_list|(
name|RemoteRefUpdate
operator|.
name|Status
operator|.
name|OK
argument_list|)
expr_stmt|;
block|}
DECL|method|isRejected (String expectedMessage)
specifier|public
name|void
name|isRejected
parameter_list|(
name|String
name|expectedMessage
parameter_list|)
block|{
name|isNotNull
argument_list|()
expr_stmt|;
name|hasStatus
argument_list|(
name|RemoteRefUpdate
operator|.
name|Status
operator|.
name|REJECTED_OTHER_REASON
argument_list|)
expr_stmt|;
name|hasMessage
argument_list|(
name|expectedMessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

