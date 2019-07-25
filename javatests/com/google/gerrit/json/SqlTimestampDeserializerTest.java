begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2019 The Android Open Source Project
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
DECL|package|com.google.gerrit.json
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|json
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
name|truth
operator|.
name|Truth
operator|.
name|assertThat
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

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|gson
operator|.
name|JsonPrimitive
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SqlTimestampDeserializerTest
specifier|public
class|class
name|SqlTimestampDeserializerTest
block|{
DECL|field|deserializer
specifier|private
specifier|final
name|SqlTimestampDeserializer
name|deserializer
init|=
operator|new
name|SqlTimestampDeserializer
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|emptyStringIsDeserializedToMagicTimestamp ()
specifier|public
name|void
name|emptyStringIsDeserializedToMagicTimestamp
parameter_list|()
block|{
name|Timestamp
name|timestamp
init|=
name|deserializer
operator|.
name|deserialize
argument_list|(
operator|new
name|JsonPrimitive
argument_list|(
literal|""
argument_list|)
argument_list|,
name|Timestamp
operator|.
name|class
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|timestamp
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|TimeUtil
operator|.
name|never
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

