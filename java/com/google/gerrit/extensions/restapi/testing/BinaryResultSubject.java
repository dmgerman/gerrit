begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|// Copyright (C) 2017 The Android Open Source Project
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
DECL|package|com.google.gerrit.extensions.restapi.testing
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|extensions
operator|.
name|restapi
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
name|truth
operator|.
name|Truth
operator|.
name|assertAbout
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
name|PrimitiveByteArraySubject
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
name|StringSubject
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
name|extensions
operator|.
name|restapi
operator|.
name|BinaryResult
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
name|truth
operator|.
name|OptionalSubject
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_class
DECL|class|BinaryResultSubject
specifier|public
class|class
name|BinaryResultSubject
extends|extends
name|Subject
argument_list|<
name|BinaryResultSubject
argument_list|,
name|BinaryResult
argument_list|>
block|{
DECL|method|assertThat (BinaryResult binaryResult)
specifier|public
specifier|static
name|BinaryResultSubject
name|assertThat
parameter_list|(
name|BinaryResult
name|binaryResult
parameter_list|)
block|{
return|return
name|assertAbout
argument_list|(
name|binaryResults
argument_list|()
argument_list|)
operator|.
name|that
argument_list|(
name|binaryResult
argument_list|)
return|;
block|}
DECL|method|binaryResults ()
specifier|private
specifier|static
name|Subject
operator|.
name|Factory
argument_list|<
name|BinaryResultSubject
argument_list|,
name|BinaryResult
argument_list|>
name|binaryResults
parameter_list|()
block|{
return|return
name|BinaryResultSubject
operator|::
operator|new
return|;
block|}
DECL|method|assertThat ( Optional<BinaryResult> binaryResultOptional)
specifier|public
specifier|static
name|OptionalSubject
argument_list|<
name|BinaryResultSubject
argument_list|,
name|BinaryResult
argument_list|>
name|assertThat
parameter_list|(
name|Optional
argument_list|<
name|BinaryResult
argument_list|>
name|binaryResultOptional
parameter_list|)
block|{
return|return
name|OptionalSubject
operator|.
name|assertThat
argument_list|(
name|binaryResultOptional
argument_list|,
name|binaryResults
argument_list|()
argument_list|)
return|;
block|}
DECL|field|binaryResult
specifier|private
specifier|final
name|BinaryResult
name|binaryResult
decl_stmt|;
DECL|method|BinaryResultSubject (FailureMetadata failureMetadata, BinaryResult binaryResult)
specifier|private
name|BinaryResultSubject
parameter_list|(
name|FailureMetadata
name|failureMetadata
parameter_list|,
name|BinaryResult
name|binaryResult
parameter_list|)
block|{
name|super
argument_list|(
name|failureMetadata
argument_list|,
name|binaryResult
argument_list|)
expr_stmt|;
name|this
operator|.
name|binaryResult
operator|=
name|binaryResult
expr_stmt|;
block|}
DECL|method|asString ()
specifier|public
name|StringSubject
name|asString
parameter_list|()
throws|throws
name|IOException
block|{
name|isNotNull
argument_list|()
expr_stmt|;
comment|// We shouldn't close the BinaryResult within this method as it might still
comment|// be used afterwards. Besides, closing it doesn't have an effect for most
comment|// implementations of a BinaryResult.
return|return
name|check
argument_list|(
literal|"asString()"
argument_list|)
operator|.
name|that
argument_list|(
name|binaryResult
operator|.
name|asString
argument_list|()
argument_list|)
return|;
block|}
DECL|method|bytes ()
specifier|public
name|PrimitiveByteArraySubject
name|bytes
parameter_list|()
throws|throws
name|IOException
block|{
name|isNotNull
argument_list|()
expr_stmt|;
comment|// We shouldn't close the BinaryResult within this method as it might still
comment|// be used afterwards. Besides, closing it doesn't have an effect for most
comment|// implementations of a BinaryResult.
name|ByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|binaryResult
operator|.
name|writeTo
argument_list|(
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|byteArrayOutputStream
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
name|check
argument_list|(
literal|"bytes()"
argument_list|)
operator|.
name|that
argument_list|(
name|bytes
argument_list|)
return|;
block|}
block|}
end_class

end_unit

