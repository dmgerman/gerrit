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
DECL|package|com.google.gerrit.server.git.gpg
package|package
name|com
operator|.
name|google
operator|.
name|gerrit
operator|.
name|server
operator|.
name|git
operator|.
name|gpg
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
import|import
name|org
operator|.
name|eclipse
operator|.
name|jgit
operator|.
name|util
operator|.
name|NB
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_class
DECL|class|Fingerprint
specifier|public
class|class
name|Fingerprint
block|{
DECL|field|fp
specifier|private
specifier|final
name|byte
index|[]
name|fp
decl_stmt|;
DECL|method|Fingerprint (byte[] fp)
specifier|public
name|Fingerprint
parameter_list|(
name|byte
index|[]
name|fp
parameter_list|)
block|{
comment|// Don't bother with defensive copies; PGPPublicKey#getFingerprint() already
comment|// does so.
name|checkArgument
argument_list|(
name|fp
operator|.
name|length
operator|==
literal|20
argument_list|,
literal|"fingerprint must be 20 bytes, got %s"
argument_list|,
name|fp
operator|.
name|length
argument_list|)
expr_stmt|;
name|this
operator|.
name|fp
operator|=
name|fp
expr_stmt|;
block|}
DECL|method|get ()
specifier|public
name|byte
index|[]
name|get
parameter_list|()
block|{
return|return
name|fp
return|;
block|}
DECL|method|equalsBytes (byte[] bytes)
specifier|public
name|boolean
name|equalsBytes
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
return|return
name|Arrays
operator|.
name|equals
argument_list|(
name|fp
argument_list|,
name|bytes
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
comment|// Same hash code as ObjectId: second int word.
return|return
name|NB
operator|.
name|decodeInt32
argument_list|(
name|fp
argument_list|,
literal|4
argument_list|)
return|;
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
return|return
operator|(
name|o
operator|instanceof
name|Fingerprint
operator|)
operator|&&
name|equalsBytes
argument_list|(
operator|(
operator|(
name|Fingerprint
operator|)
name|o
operator|)
operator|.
name|fp
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
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|fp
argument_list|)
decl_stmt|;
return|return
name|String
operator|.
name|format
argument_list|(
literal|"(%04X %04X %04X %04X %04X  %04X %04X %04X %04X %04X)"
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|,
name|buf
operator|.
name|getShort
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getId ()
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|fp
argument_list|)
operator|.
name|getLong
argument_list|(
literal|12
argument_list|)
return|;
block|}
block|}
end_class

end_unit

