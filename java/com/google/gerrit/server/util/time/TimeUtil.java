begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2013 The Android Open Source Project
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
DECL|package|com.google.gerrit.server.util.time
package|package
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
package|;
end_package

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
name|gerrit
operator|.
name|common
operator|.
name|UsedAt
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
name|UsedAt
operator|.
name|Project
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
name|git
operator|.
name|DelegateSystemReader
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
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|LongSupplier
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
name|util
operator|.
name|SystemReader
import|;
end_import

begin_comment
comment|/** Static utility methods for dealing with dates and times. */
end_comment

begin_class
DECL|class|TimeUtil
specifier|public
class|class
name|TimeUtil
block|{
DECL|field|SYSTEM_CURRENT_MILLIS_SUPPLIER
specifier|private
specifier|static
specifier|final
name|LongSupplier
name|SYSTEM_CURRENT_MILLIS_SUPPLIER
init|=
name|System
operator|::
name|currentTimeMillis
decl_stmt|;
DECL|field|currentMillisSupplier
specifier|private
specifier|static
specifier|volatile
name|LongSupplier
name|currentMillisSupplier
init|=
name|SYSTEM_CURRENT_MILLIS_SUPPLIER
decl_stmt|;
DECL|method|nowMs ()
specifier|public
specifier|static
name|long
name|nowMs
parameter_list|()
block|{
comment|// We should rather use Instant.now(Clock).toEpochMilli() instead but this would require some
comment|// changes in our testing code as we wouldn't have clock steps anymore.
return|return
name|currentMillisSupplier
operator|.
name|getAsLong
argument_list|()
return|;
block|}
DECL|method|now ()
specifier|public
specifier|static
name|Instant
name|now
parameter_list|()
block|{
return|return
name|Instant
operator|.
name|ofEpochMilli
argument_list|(
name|nowMs
argument_list|()
argument_list|)
return|;
block|}
DECL|method|nowTs ()
specifier|public
specifier|static
name|Timestamp
name|nowTs
parameter_list|()
block|{
return|return
operator|new
name|Timestamp
argument_list|(
name|nowMs
argument_list|()
argument_list|)
return|;
block|}
comment|/**    * Returns the magic timestamp representing no specific time.    *    *<p>This "null object" is helpful in contexts where using {@code null} directly is not possible.    */
annotation|@
name|UsedAt
argument_list|(
name|Project
operator|.
name|PLUGIN_CHECKS
argument_list|)
DECL|method|never ()
specifier|public
specifier|static
name|Timestamp
name|never
parameter_list|()
block|{
comment|// Always create a new object as timestamps are mutable.
return|return
operator|new
name|Timestamp
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|truncateToSecond (Timestamp t)
specifier|public
specifier|static
name|Timestamp
name|truncateToSecond
parameter_list|(
name|Timestamp
name|t
parameter_list|)
block|{
return|return
operator|new
name|Timestamp
argument_list|(
operator|(
name|t
operator|.
name|getTime
argument_list|()
operator|/
literal|1000
operator|)
operator|*
literal|1000
argument_list|)
return|;
block|}
annotation|@
name|VisibleForTesting
DECL|method|setCurrentMillisSupplier (LongSupplier customCurrentMillisSupplier)
specifier|public
specifier|static
name|void
name|setCurrentMillisSupplier
parameter_list|(
name|LongSupplier
name|customCurrentMillisSupplier
parameter_list|)
block|{
name|currentMillisSupplier
operator|=
name|customCurrentMillisSupplier
expr_stmt|;
name|SystemReader
name|oldSystemReader
init|=
name|SystemReader
operator|.
name|getInstance
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|oldSystemReader
operator|instanceof
name|GerritSystemReader
operator|)
condition|)
block|{
name|SystemReader
operator|.
name|setInstance
argument_list|(
operator|new
name|GerritSystemReader
argument_list|(
name|oldSystemReader
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|VisibleForTesting
DECL|method|resetCurrentMillisSupplier ()
specifier|public
specifier|static
name|void
name|resetCurrentMillisSupplier
parameter_list|()
block|{
name|currentMillisSupplier
operator|=
name|SYSTEM_CURRENT_MILLIS_SUPPLIER
expr_stmt|;
name|SystemReader
operator|.
name|setInstance
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|class|GerritSystemReader
specifier|static
class|class
name|GerritSystemReader
extends|extends
name|DelegateSystemReader
block|{
DECL|method|GerritSystemReader (SystemReader reader)
name|GerritSystemReader
parameter_list|(
name|SystemReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCurrentTime ()
specifier|public
name|long
name|getCurrentTime
parameter_list|()
block|{
return|return
name|currentMillisSupplier
operator|.
name|getAsLong
argument_list|()
return|;
block|}
block|}
DECL|method|TimeUtil ()
specifier|private
name|TimeUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

