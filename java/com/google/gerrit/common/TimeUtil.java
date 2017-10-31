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
DECL|package|com.google.gerrit.common
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|common
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
name|GwtIncompatible
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
name|function
operator|.
name|LongSupplier
import|;
end_import

begin_comment
comment|/** Static utility methods for dealing with dates and times. */
end_comment

begin_class
annotation|@
name|GwtIncompatible
argument_list|(
literal|"Unemulated Java 8 functionalities"
argument_list|)
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
DECL|method|roundToSecond (Timestamp t)
specifier|public
specifier|static
name|Timestamp
name|roundToSecond
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
block|}
DECL|method|TimeUtil ()
specifier|private
name|TimeUtil
parameter_list|()
block|{}
block|}
end_class

end_unit

